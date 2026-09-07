package com.iafenvoy.tooltipsreforged.component;

//? if <1.21 {
import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.util.InfoCollectHelper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ContainerPreviewComponent implements ClientTooltipComponent {
    private static final ResourceLocation BACKGROUND = ResourceLocation.tryBuild(TooltipReforgedClient.MOD_ID, "textures/gui/container_preview.png");
    @Nullable
    private final NonNullList<ItemStack> stacks;

    public ContainerPreviewComponent(ItemStack stack) {
        this.stacks = InfoCollectHelper.collectContainer(stack);
    }

    private boolean shouldRender() {
        return this.stacks != null && TooltipReforgedConfig.INSTANCE.tooltip.containerTooltip.getValue().shouldRenderImage();
    }

    @Override
    public int getHeight() {
        return this.shouldRender() ? 69 : 0;
    }

    @Override
    public int getWidth(Font textRenderer) {
        return this.shouldRender() ? 175 : 0;
    }

    @Override
    public void renderImage(Font textRenderer, int x, int y, GuiGraphics context) {
        if (this.stacks == null || !this.shouldRender()) return;
        context.blit(BACKGROUND, x, y, 0, 0, 256, 256);
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 9; j++) {
                int index = i * 9 + j;
                if (this.stacks.size() > index) {
                    ItemStack stack = this.stacks.get(index);
                    context.renderItem(stack, x + 8 + j * 18, y + 8 + i * 18);
                    context.renderItemDecorations(textRenderer, stack, x + 8 + j * 18, y + 8 + i * 18);
                }
            }
    }
}

//?} else {
import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.util.InfoCollectHelper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ContainerPreviewComponent implements ClientTooltipComponent {
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(TooltipReforgedClient.MOD_ID, "textures/gui/container_preview.png");
    @Nullable
    private final NonNullList<ItemStack> stacks;

    public ContainerPreviewComponent(ItemStack stack, HolderLookup.Provider registries) {
        this.stacks = InfoCollectHelper.collectContainer(stack, registries);
    }

    private boolean shouldRender() {
        return this.stacks != null && TooltipReforgedConfig.INSTANCE.tooltip.containerTooltip.getValue().shouldRenderImage();
    }

    @Override
    public int getHeight() {
        return this.shouldRender() ? 69 : 0;
    }

    @Override
    public int getWidth(Font textRenderer) {
        return this.shouldRender() ? 175 : 0;
    }

    @Override
    public void renderImage(Font textRenderer, int x, int y, GuiGraphics context) {
        if (this.stacks == null || !this.shouldRender()) return;
        context.blit(BACKGROUND, x, y, 0, 0, 256, 256);
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 9; j++) {
                int index = i * 9 + j;
                if (this.stacks.size() > index) {
                    ItemStack stack = this.stacks.get(index);
                    context.renderItem(stack, x + 8 + j * 18, y + 8 + i * 18);
                    context.renderItemDecorations(textRenderer, stack, x + 8 + j * 18, y + 8 + i * 18);
                }
            }
    }
}

//?}
