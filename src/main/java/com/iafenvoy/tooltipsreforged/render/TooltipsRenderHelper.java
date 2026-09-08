package com.iafenvoy.tooltipsreforged.render;

import com.iafenvoy.integration.entrypoint.EntryPointManager;
import com.iafenvoy.tooltipsreforged.BuiltinTooltips;
import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import com.iafenvoy.tooltipsreforged.api.TooltipsReforgeEntrypoint;
import com.iafenvoy.tooltipsreforged.component.StandaloneComponent;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.util.ExtendedTextVisitor;
import com.iafenvoy.tooltipsreforged.util.TextUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
//? >=1.21 {
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.RegistryAccess;
//?}
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector2ic;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class TooltipsRenderHelper {
    private static final int EDGE_SPACING = 32;
    private static final int PAGE_SPACING = 12;

    public static void render(ItemStack stack, List<ClientTooltipComponent> components, GuiGraphics context, Font textRenderer, int mouseX, int mouseY, ClientTooltipPositioner positioner) {
        //? if <1.21 {
        /*BuiltinTooltips.appendTooltip(stack, components);
        EntryPointManager.getEntryPoints(TooltipReforgedClient.MOD_ID, TooltipsReforgeEntrypoint.class).forEach(e -> e.appendTooltip(stack, components));
        *///?} else {
        ClientLevel world = Minecraft.getInstance().level;
        if (world == null) return;
        RegistryAccess registries = world.registryAccess();
        BuiltinTooltips.appendTooltip(stack, components, registries);
        EntryPointManager.getEntryPoints(TooltipReforgedClient.MOD_ID, TooltipsReforgeEntrypoint.class).forEach(e -> e.appendTooltip(stack, components, registries));
        //?}
        components.removeIf(Objects::isNull);
        if (TooltipReforgedConfig.INSTANCE.misc.removeEmptyLines.getValue())
            components.removeIf(x -> x instanceof ClientTextTooltip ordered && ExtendedTextVisitor.getText(TextUtil.getTextFromComponent(ordered)).getString().isEmpty());
        ResolveResult result = resolveTooltips(textRenderer, components);
        Vector2ic position = resolvePosition(result, context, mouseX, mouseY, positioner);
        drawWithResult(result, stack, context, textRenderer, position.x(), position.y());
    }

    public static ResolveResult resolveTooltips(Font textRenderer, List<ClientTooltipComponent> components) {
        if (components.isEmpty()) return new ResolveResult(List.of(), List.of(), 0);
        List<Page> pageList = new LinkedList<>();
        List<StandaloneComponent> standaloneComponents = new LinkedList<>();
        int maxWidth = getMaxWidth(), maxHeight = getMaxHeight();
        int totalWidth = 0, pageHeight = -2;
        Page p = new Page();
        for (ClientTooltipComponent component : components) {
            if (component instanceof StandaloneComponent standalone) {
                standaloneComponents.add(standalone);
                continue;
            }
            List<ClientTooltipComponent> lines = component.getWidth(textRenderer) > maxWidth ? wrapTextComponent(textRenderer, component, maxWidth) : List.of(component);
            for (ClientTooltipComponent line : lines) {
                int lineWidth = line.getWidth(textRenderer);
                int lineHeight = line.getHeight();

                if (pageHeight + lineHeight > maxHeight) {
                    pageList.add(p);
                    totalWidth += p.width;
                    p = new Page();
                    pageHeight = -2;
                }

                p.components.add(line);
                p.height = pageHeight += lineHeight;
                p.width = Math.max(p.width, lineWidth);
            }
        }
        if (!p.components.isEmpty()) {
            pageList.add(p);
            totalWidth += p.width;
        }
        return new ResolveResult(pageList, standaloneComponents, totalWidth);
    }

    public static Vector2ic resolvePosition(ResolveResult result, GuiGraphics context, int mouseX, int mouseY, ClientTooltipPositioner positioner) {
        int width = result.totalWidth + PAGE_SPACING * Math.max(0, result.pages.size() - 1);
        return positioner.positionTooltip(context.guiWidth(), context.guiHeight(), mouseX, mouseY, width, firstPage(result.pages).height);
    }

    public static void drawWithResult(ResolveResult result, ItemStack stack, GuiGraphics context, Font textRenderer, int x, int y) {
        if (result.pages.isEmpty()) return;
        //Set actual position
        int currentX = x;
        for (Page page : result.pages) {
            page.x = currentX;
            page.y = y - (result.pages.size() > 1 ? EDGE_SPACING : 6);
            currentX += page.width + PAGE_SPACING;
        }
        //Render start
        context.pose().pushPose();
        context.pose().translate(0, 0, 400);
        for (Page page : result.pages) {
            ExtendedTooltipBackgroundRenderer.render(stack, context, page.x, page.y, page.width, page.height, 0);
            int currentY = page.y;
            for (ClientTooltipComponent component : page.components) {
                try {
                    component.renderText(textRenderer, page.x, currentY, context.pose().last().pose(), context.bufferSource());
                    component.renderImage(textRenderer, page.x, currentY, context);
                    currentY += component.getHeight();
                } catch (Exception e) {
                    TooltipReforgedClient.LOGGER.error("{}", TooltipReforgedClient.MOD_ID, e);
                }
            }
        }
        for (StandaloneComponent component : result.standalone)
            try {
                component.render(context, textRenderer, x, y, result.totalWidth, 0);
            } catch (Exception e) {
                TooltipReforgedClient.LOGGER.error("{}", TooltipReforgedClient.MOD_ID, e);
            }
        context.pose().popPose();
    }

    private static List<ClientTooltipComponent> wrapTextComponent(Font textRenderer, ClientTooltipComponent component, int maxWidth) {
        if (!(component instanceof ClientTextTooltip text) || component.getWidth(textRenderer) <= maxWidth) return List.of(component);
        return textRenderer.split(ExtendedTextVisitor.getText(TextUtil.getTextFromComponent(text)), maxWidth)
                .stream().map(ClientTooltipComponent::create).toList();
    }

    private static int getMaxHeight() {
        return Minecraft.getInstance().getWindow().getGuiScaledHeight() - EDGE_SPACING * 2;
    }

    private static int getMaxWidth() {
        return Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 - EDGE_SPACING;
    }

    public static void drawNineSlicedTexture(GuiGraphics context, ResourceLocation texture, int x, int y, int width, int height, int border, int textureWidth, int textureHeight) {
        context.blit(texture, x, y, 0, 0, border, border, textureWidth, textureHeight);
        drawRepeatingTexture(context, texture, x + border, y, width - border * 2, border, border, 0, textureWidth - border * 2, border, textureWidth, textureHeight);
        context.blit(texture, x + width - border, y, textureWidth - border, 0, border, border, textureWidth, textureHeight);

        drawRepeatingTexture(context, texture, x, y + border, border, height - border * 2, 0, border, border, textureHeight - border * 2, textureWidth, textureHeight);
        drawRepeatingTexture(context, texture, x + border, y + border, width - border * 2, height - border * 2, border, border, textureWidth - border * 2, textureHeight - border * 2, textureWidth, textureHeight);
        drawRepeatingTexture(context, texture, x + width - border, y + border, border, height - border * 2, textureWidth - border, border, border, textureHeight - border * 2, textureWidth, textureHeight);

        context.blit(texture, x, y + height - border, 0, textureHeight - border, border, border, textureWidth, textureHeight);
        drawRepeatingTexture(context, texture, x + border, y + height - border, width - border * 2, border, border, textureHeight - border, textureWidth - border * 2, border, textureWidth, textureHeight);
        context.blit(texture, x + width - border, y + height - border, textureWidth - border, textureHeight - border, border, border, textureWidth, textureHeight);
    }

    public static void drawRepeatingTexture(GuiGraphics context, ResourceLocation texture, int x, int y, int width, int height, int u, int v, int regionWidth, int regionHeight, int textureWidth, int textureHeight) {
        int i, j;
        for (i = 0; i + regionWidth < width; i += regionWidth) {
            for (j = 0; j + regionHeight < height; j += regionHeight)
                context.blit(texture, x + i, y + j, u, v, regionWidth, regionHeight, textureWidth, textureHeight);
            context.blit(texture, x + i, y + j, u, v, regionWidth, height - j, textureWidth, textureHeight);
        }
        for (j = 0; j + regionHeight < height; j += regionHeight)
            context.blit(texture, x + i, y + j, u, v, width - i, regionHeight, textureWidth, textureHeight);
        context.blit(texture, x + i, y + j, u, v, width - i, height - j, textureWidth, textureHeight);
    }

    public record ResolveResult(List<Page> pages, List<StandaloneComponent> standalone, int totalWidth) {
    }

    public static class Page {
        private int x;
        private int y;
        private int width;
        private int height;
        private final List<ClientTooltipComponent> components;

        private Page() {
            this(0, 0, 0, 0, new LinkedList<>());
        }

        private Page(int x, int y, int width, int height, List<ClientTooltipComponent> components) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.components = components;
        }

        public int getHeight() {
            return this.height;
        }
    }

    private static Page firstPage(List<Page> pages) {
        //? if <1.21 {
        /*return pages.get(0);
        *///?} else {
        return pages.getFirst();
        //?}
    }
}

