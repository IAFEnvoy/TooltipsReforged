package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.render.ExtendedTooltipBackgroundRenderer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;

public abstract class StandaloneComponent implements ClientTooltipComponent {
    private static final int EDGE_SPACING = 12;
    private static final int FRAME_GAP = 6;
    protected final ItemStack stack;

    public StandaloneComponent(ItemStack stack) {
        this.stack = stack;
    }

    public abstract void render(GuiGraphics context, Font textRenderer, int x, int y, int tooltipWidth, int z);

    /**
     * Resolves the horizontal position of a side frame that is normally placed on the left side of the tooltip.
     * If there is not enough room on the left (e.g. the tooltip itself is already hugging the left screen edge),
     * the frame is flipped to the right side of the tooltip so that it stays fully visible.
     */
    protected int getFrameX(GuiGraphics context, int x, int tooltipWidth, int frameWidth, int leftOffset) {
        int leftX = x + leftOffset;
        if (leftX - EDGE_SPACING >= 0) return leftX;
        int rightX = x + tooltipWidth + FRAME_GAP;
        if (rightX + frameWidth + EDGE_SPACING <= context.guiWidth()) return rightX;
        return leftX;
    }

    protected void renderBackground(GuiGraphics context, int x, int y, int width, int height, int z) {
        ExtendedTooltipBackgroundRenderer.render(this.stack, context, x, y, width, height, z);
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int getWidth(Font textRenderer) {
        return 0;
    }
}
