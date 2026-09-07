package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.config.mode.ItemDisplayMode;
import com.iafenvoy.tooltipsreforged.render.RenderHelper;
import com.iafenvoy.tooltipsreforged.render.TooltipProviders;
import com.iafenvoy.tooltipsreforged.util.BadgesUtils;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class HeaderComponent implements ClientTooltipComponent, RenderHelper {
    protected final ItemStack stack;
    private final ItemDisplayMode itemDisplayMode;
    private final FormattedCharSequence nameText, rarityName;
    private final Pair<Component, Integer> badgePair;

    public HeaderComponent(ItemStack stack, @Nullable FormattedCharSequence original) {
        this.stack = stack;
        this.itemDisplayMode = TooltipReforgedConfig.INSTANCE.tooltip.itemDisplayTooltip.getValue();
        this.nameText = TooltipReforgedConfig.INSTANCE.misc.useOriginalTitle.getValue() && original != null ? original : TooltipProviders.getDisplayName(this.stack).getVisualOrderText();
        this.rarityName = TooltipProviders.getRarityName(this.stack).getVisualOrderText();
        this.badgePair = BadgesUtils.getBadgeText(this.stack);
    }

    @Override
    public int getHeight() {
        return 24;
    }

    @Override
    public int getWidth(Font textRenderer) {
        Component badgeText = this.badgePair.getFirst();
        int rarityWidth = TooltipReforgedConfig.INSTANCE.tooltip.rarityTooltip.getValue() ? textRenderer.width(this.rarityName) : 0;
        int badgeWidth = TooltipReforgedConfig.INSTANCE.tooltip.itemGroupTooltip.getValue() ? textRenderer.width(badgeText) + SPACING * 2 : 0;
        int titleWidth = TooltipReforgedConfig.INSTANCE.tooltip.rarityTooltip.getValue() ? textRenderer.width(this.nameText) + badgeWidth + 2 : Math.max(textRenderer.width(this.nameText), badgeWidth);
        return Math.max(titleWidth, rarityWidth) + this.getTitleOffset();
    }

    public int getTitleOffset() {
        return this.itemDisplayMode.shouldRenderHeader() ? 24 : 0;
    }

    @Override
    public void renderText(Font textRenderer, int x, int y, Matrix4f matrix, MultiBufferSource.BufferSource vertexConsumers) {
        float startDrawX = (float) x + this.getTitleOffset();
        float startDrawY = y + 1;
        textRenderer.drawInBatch(this.nameText, startDrawX, startDrawY - 1, -1, true, matrix, vertexConsumers, Font.DisplayMode.NORMAL, 0, 0xF000F0);
        if (TooltipReforgedConfig.INSTANCE.tooltip.rarityTooltip.getValue()) {
            startDrawY += textRenderer.lineHeight + 2;
            textRenderer.drawInBatch(this.rarityName, startDrawX, startDrawY, -1, true, matrix, vertexConsumers, Font.DisplayMode.NORMAL, 0, 0xF000F0);
        }
    }

    @Override
    public void renderImage(Font textRenderer, int x, int y, GuiGraphics context) {
        int startDrawX = x + 2;
        int startDrawY = y + 2;
        if (this.itemDisplayMode.shouldRenderHeader()) {
            context.renderItem(this.stack, startDrawX, startDrawY);
            int color = TooltipReforgedConfig.INSTANCE.misc.itemBorderColor.getValue();
            context.fill(startDrawX - 3, startDrawY - 2, startDrawX - 3 + 1, startDrawY - 2 + 20, 0, color);
            context.fill(startDrawX + 18, startDrawY - 2, startDrawX + 18 + 1, startDrawY - 2 + 20, 0, color);
            context.fill(startDrawX - 2, startDrawY - 3, startDrawX - 2 + 20, startDrawY - 3 + 1, 0, color);
            context.fill(startDrawX - 2, startDrawY + 18, startDrawX - 2 + 20, startDrawY + 18 + 1, 0, color);
        }
        if (TooltipReforgedConfig.INSTANCE.tooltip.itemGroupTooltip.getValue()) {
            Component text = this.badgePair.getFirst();
            int fillColor = this.badgePair.getSecond();
            int textWidth = textRenderer.width(text);
            int textHeight = textRenderer.lineHeight - 2;
            int textX = x + this.getTitleOffset() + (TooltipReforgedConfig.INSTANCE.tooltip.rarityTooltip.getValue() ? textRenderer.width(this.nameText) + SPACING + 2 : 4);
            int textY = (TooltipReforgedConfig.INSTANCE.tooltip.rarityTooltip.getValue() ? y - 2 : y - 2 + 12) - textRenderer.lineHeight + SPACING * 2 + 4;
            context.fill(textX - SPACING, textY - SPACING / 2, textX + textWidth + SPACING, textY + textHeight, BadgesUtils.darkenColor(fillColor, 0.9f));
            context.drawString(textRenderer, text, textX, textY - 1, 0xffffffff, true);
            BadgesUtils.drawFrame(context, textX - SPACING, textY - SPACING / 2, textWidth + SPACING * 2, textHeight + SPACING, 400, BadgesUtils.darkenColor(fillColor, 0.8f));
        }
    }
}

