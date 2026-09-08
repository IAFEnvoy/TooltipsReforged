package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.config.mode.ItemDisplayMode;
import com.iafenvoy.tooltipsreforged.render.RenderHelper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

public class ItemZoomComponent extends StandaloneComponent implements RenderHelper {
    private final ItemDisplayMode itemDisplayMode;

    public ItemZoomComponent(ItemStack stack) {
        super(stack);
        this.itemDisplayMode = TooltipReforgedConfig.INSTANCE.tooltip.itemDisplayTooltip.getValue();
    }

    @Override
    public void render(GuiGraphics context, Font textRenderer, int x, int y, int tooltipWidth, int z) {
        if (this.itemDisplayMode.shouldRenderZoom()) {
            int size = TooltipReforgedConfig.INSTANCE.misc.itemZoomSize.getValue();
            int zoomX = context.guiWidth() / 2 - size / 2 - 170 + TooltipReforgedConfig.INSTANCE.misc.itemZoomXOffset.getValue();
            int zoomY = context.guiHeight() / 2 - size / 2 + TooltipReforgedConfig.INSTANCE.misc.itemZoomYOffset.getValue();
            this.drawStack(context, this.stack, zoomX, zoomY, size);
        }
    }
}

