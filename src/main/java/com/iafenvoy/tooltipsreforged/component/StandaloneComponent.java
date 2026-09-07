package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.render.ExtendedTooltipBackgroundRenderer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;

public abstract class StandaloneComponent implements ClientTooltipComponent {
    protected final ItemStack stack;

    public StandaloneComponent(ItemStack stack) {
        this.stack = stack;
    }

    public abstract void render(GuiGraphics context, Font textRenderer, int x, int y, int z);

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

