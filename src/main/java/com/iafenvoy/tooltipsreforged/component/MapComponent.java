package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.MapRenderer;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
//? >=1.21 {
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.level.saveddata.maps.MapId;
//?}
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class MapComponent implements ClientTooltipComponent {
    protected final ItemStack stack;

    public MapComponent(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public int getHeight() {
        return TooltipReforgedConfig.INSTANCE.tooltip.mapTooltip.getValue() ? 130 : 0;
    }

    @Override
    public int getWidth(Font textRenderer) {
        return TooltipReforgedConfig.INSTANCE.tooltip.mapTooltip.getValue() ? 128 : 0;
    }

    @Override
    public void renderImage(Font textRenderer, int x, int y, GuiGraphics context) {
        if (!TooltipReforgedConfig.INSTANCE.tooltip.mapTooltip.getValue()) return;
        MultiBufferSource vertexConsumers = Minecraft.getInstance().renderBuffers().bufferSource();
        MapRenderer mapRenderer = Minecraft.getInstance().gameRenderer.getMapRenderer();

        //? if <1.21 {
        /*Integer mapId = MapItem.getMapId(this.stack);
        *///?} else {
        MapId mapId = this.stack.get(DataComponents.MAP_ID);
        //?}
        MapItemSavedData mapState = MapItem.getSavedData(this.stack, Minecraft.getInstance().level);
        if (mapId == null || mapState == null) return;

        PoseStack matrices = context.pose();
        matrices.pushPose();
        matrices.translate(x, y, 0);
        matrices.scale(1, 1, 0);
        mapRenderer.update(mapId, mapState);
        mapRenderer.render(matrices, vertexConsumers, mapId, mapState, false, LightTexture.FULL_BRIGHT);
        matrices.popPose();
    }
}
