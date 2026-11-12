package com.iafenvoy.tooltipsreforged.component;

import com.google.common.collect.ImmutableList;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.util.RLUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Holder;
//? if <1.21 {
/*import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.BlockItem;
*///?} else {
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
//?}
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BannerPatternTags;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPattern;

import java.util.ArrayList;
import java.util.List;

public class BannerPatternComponent implements ClientTooltipComponent {
    // The vanilla banner base and pattern textures share the same 64x64 canvas, the flag itself is
    // drawn in the top left 22x42 area.
    private static final ResourceLocation BASE_TEXTURE = RLUtil.id("minecraft", "textures/entity/banner/base.png");
    private static final int PREVIEW_WIDTH = 22;
    private static final int PREVIEW_HEIGHT = 42;
    private static final int TEXTURE_SIZE = 64;
    private final List<PreviewLayer> layers;

    public BannerPatternComponent(ItemStack stack/*? >=1.21 {*/, RegistryAccess registries/*?}*/) {
        this.layers = getLayers(stack/*? >=1.21 {*/, registries/*?}*/);
    }

    private boolean shouldRender() {
        return !this.layers.isEmpty() && TooltipReforgedConfig.INSTANCE.tooltip.bannerTooltip.getValue();
    }

    @Override
    public int getHeight() {
        return this.shouldRender() ? 50 : 0;
    }

    @Override
    public int getWidth(Font textRenderer) {
        return this.shouldRender() ? 20 : 0;
    }

    @Override
    public void renderImage(Font textRenderer, int x, int y, GuiGraphics context) {
        if (!this.shouldRender()) return;
        for (PreviewLayer layer : this.layers) {
            context.setColor(layer.red(), layer.green(), layer.blue(), 1.0F);
            context.blit(layer.texture(), x, y, PREVIEW_WIDTH, PREVIEW_HEIGHT, 0, 0, PREVIEW_WIDTH, PREVIEW_HEIGHT, TEXTURE_SIZE, TEXTURE_SIZE);
        }
        context.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private static List<PreviewLayer> getLayers(ItemStack stack/*? >=1.21 {*/, RegistryAccess registries/*?}*/) {
        if (stack.getItem() instanceof BannerItem banner) return getBannerLayers(stack, banner);
        else return getPatternItemLayers(stack/*? >=1.21 {*/, registries/*?}*/);
    }

    //? if <1.21 {
    /*private static List<PreviewLayer> getBannerLayers(ItemStack stack, BannerItem banner) {
        List<PreviewLayer> layers = new ArrayList<>();
        layers.add(create(BASE_TEXTURE, banner.getColor()));
        CompoundTag nbt = BlockItem.getBlockEntityData(stack);
        if (nbt == null || !nbt.contains("Patterns", Tag.TAG_LIST)) return layers;
        ListTag patterns = nbt.getList("Patterns", Tag.TAG_COMPOUND);
        for (int i = 0; i < patterns.size(); i++) {
            CompoundTag patternTag = patterns.getCompound(i);
            Holder<BannerPattern> pattern = BannerPattern.byHash(patternTag.getString("Pattern"));
            if (pattern == null) continue;
            Material material = pattern.unwrapKey().map(Sheets::getBannerMaterial).orElse(null);
            if (material != null)
                layers.add(create(material, DyeColor.byId(patternTag.getInt("Color"))));
        }
        return layers;
    }
    *///?} else {
    private static List<PreviewLayer> getBannerLayers(ItemStack stack, BannerItem banner) {
        List<PreviewLayer> layers = new ArrayList<>();
        layers.add(create(BASE_TEXTURE, banner.getColor()));
        for (BannerPatternLayers.Layer layer : stack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY).layers())
            layers.add(create(Sheets.getBannerMaterial(layer.pattern()), layer.color()));
        return layers;
    }
    //?}

    //? if <1.21 {
    /*private static List<PreviewLayer> getPatternItemLayers(ItemStack stack) {
        return getPatternsFor(stack).stream().findFirst().map(Holder::unwrapKey).flatMap(x -> x)
                .map(Sheets::getBannerMaterial).filter(m -> m != null)
                .map(m -> create(m, DyeColor.WHITE)).map(List::of).orElse(List.of());
    }
    *///?} else {
    private static List<PreviewLayer> getPatternItemLayers(ItemStack stack, RegistryAccess registries) {
        return getPatternsFor(stack, registries).stream().findFirst().map(Sheets::getBannerMaterial).filter(m -> m != null)
                .map(m -> create(m, DyeColor.WHITE)).map(List::of).orElse(List.of());
    }
    //?}

    private static List<Holder<BannerPattern>> getPatternsFor(ItemStack stack/*? >=1.21 {*/, RegistryAccess registries/*?}*/) {
        //? if <1.21 {
        /*if (stack.isEmpty())
            return BuiltInRegistries.BANNER_PATTERN.getTag(BannerPatternTags.NO_ITEM_REQUIRED).map(ImmutableList::copyOf).orElse(ImmutableList.of());
        else if (stack.getItem() instanceof BannerPatternItem pattern)
            return BuiltInRegistries.BANNER_PATTERN.getTag(pattern.getBannerPattern()).map(ImmutableList::copyOf).orElse(ImmutableList.of());
        else return List.of();
        *///?} else {
        Registry<BannerPattern> registry = registries.registryOrThrow(Registries.BANNER_PATTERN);
        if (stack.isEmpty())
            return registry.getTag(BannerPatternTags.NO_ITEM_REQUIRED).map(ImmutableList::copyOf).orElse(ImmutableList.of());
        else if (stack.getItem() instanceof BannerPatternItem pattern)
            return registry.getTag(pattern.getBannerPattern()).map(ImmutableList::copyOf).orElse(ImmutableList.of());
        else return List.of();
        //?}
    }

    private static PreviewLayer create(Material material, DyeColor color) {
        return create(material.texture().withPrefix("textures/").withSuffix(".png"), color);
    }

    private static PreviewLayer create(ResourceLocation texture, DyeColor color) {
        //? if <1.21 {
        /*float[] rgb = color.getTextureDiffuseColors();
        return new PreviewLayer(texture, rgb[0], rgb[1], rgb[2]);
        *///?} else {
        int argb = color.getTextureDiffuseColor();
        return new PreviewLayer(texture, ((argb >> 16) & 0xFF) / 255.0F, ((argb >> 8) & 0xFF) / 255.0F, (argb & 0xFF) / 255.0F);
        //?}
    }

    private record PreviewLayer(ResourceLocation texture, float red, float green, float blue) {
    }
}
