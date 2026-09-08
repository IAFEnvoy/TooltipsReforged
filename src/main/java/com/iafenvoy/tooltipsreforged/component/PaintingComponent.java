package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.ItemStack;
//? >=1.21 {
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
//?}

public class PaintingComponent implements ClientTooltipComponent {
    // Each block of a painting canvas is a 16x16 texture section, so the motive sprite that
    // covers the whole canvas is (width * 16) x (height * 16) pixels at native resolution.
    private static final int TEXTURE_SIZE = 16;
    private final PaintingVariant variant;
    private final int width, height;

    public PaintingComponent(ItemStack stack) {
        //? if <1.21 {
        /*CompoundTag nbtCompound = stack.getTagElement("EntityTag");
        *///?} else {
        CompoundTag nbtCompound = stack.getOrDefault(DataComponents.ENTITY_DATA, CustomData.EMPTY).copyTag();
        //?}
        Painting painting = EntityType.PAINTING.create(Minecraft.getInstance().level);
        if (!nbtCompound.isEmpty() && painting != null) {
            painting.load(nbtCompound.copy());
            this.variant = painting.getVariant().value();
            //? if <1.21 {
            /*// Before 1.21 variants already store their size in texture pixels (16 px per block).
            this.width = this.variant.getWidth();
            this.height = this.variant.getHeight();
            *///?} else {
            // Since 1.21 variants store their size in blocks, convert them to texture pixels.
            this.width = this.variant.width() * TEXTURE_SIZE;
            this.height = this.variant.height() * TEXTURE_SIZE;
            //?}
        } else {
            this.variant = null;
            this.width = 0;
            this.height = 0;
        }
    }

    @Override
    public int getHeight() {
        return TooltipReforgedConfig.INSTANCE.tooltip.paintingTooltip.getValue() ? this.height : 0;
    }

    @Override
    public int getWidth(Font textRenderer) {
        return TooltipReforgedConfig.INSTANCE.tooltip.paintingTooltip.getValue() ? this.width : 0;
    }

    @Override
    public void renderImage(Font textRenderer, int x, int y, GuiGraphics context) {
        if (this.variant == null || !TooltipReforgedConfig.INSTANCE.tooltip.paintingTooltip.getValue()) return;
        TextureAtlasSprite sprite = Minecraft.getInstance().getPaintingTextures().get(this.variant);
        context.blit(x, y, 0, this.width, this.height, sprite);
    }
}
