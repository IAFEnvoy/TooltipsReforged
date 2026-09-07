package com.iafenvoy.tooltipsreforged.component;

//? if <1.21 {
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

public class PaintingComponent implements ClientTooltipComponent {
    private final PaintingVariant variant;
    private final int width, height;

    public PaintingComponent(ItemStack stack) {
        CompoundTag nbtCompound = stack.getTagElement("EntityTag");
        Painting painting = EntityType.PAINTING.create(Minecraft.getInstance().level);
        if (nbtCompound != null && painting != null) {
            painting.load(nbtCompound.copy());
            this.variant = painting.getVariant().value();
            this.width = this.variant.getWidth() * 2;
            this.height = this.variant.getHeight() * 2;
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

//?} else {
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public class PaintingComponent implements ClientTooltipComponent {
    private final PaintingVariant variant;
    private final int width, height;

    public PaintingComponent(ItemStack stack) {
        CompoundTag nbtCompound = stack.getOrDefault(DataComponents.ENTITY_DATA, CustomData.EMPTY).copyTag();
        Painting painting = EntityType.PAINTING.create(Minecraft.getInstance().level);
        if (nbtCompound != null && painting != null) {
            painting.load(nbtCompound.copy());
            this.variant = painting.getVariant().value();
            this.width = this.variant.width() * 2;
            this.height = this.variant.height() * 2;
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

//?}
