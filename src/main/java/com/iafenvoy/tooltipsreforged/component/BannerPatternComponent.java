package com.iafenvoy.tooltipsreforged.component;

import com.google.common.collect.ImmutableList;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Holder;
//? if <1.21 {
/*import net.minecraft.core.registries.BuiltInRegistries;
*///?} else {
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
//?}
import net.minecraft.tags.BannerPatternTags;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPattern;

import java.util.List;

public class BannerPatternComponent implements ClientTooltipComponent {
    private final Material texture;

    public BannerPatternComponent(ItemStack stack/*? >=1.21 {*/, RegistryAccess registries/*?}*/) {
        this.texture = this.getTexture(stack/*? >=1.21 {*/, registries/*?}*/);
    }

    @Override
    public int getHeight() {
        return this.texture != null && TooltipReforgedConfig.INSTANCE.tooltip.bannerTooltip.getValue() ? 50 : 0;
    }

    @Override
    public int getWidth(Font textRenderer) {
        return this.texture != null && TooltipReforgedConfig.INSTANCE.tooltip.bannerTooltip.getValue() ? 20 : 0;
    }

    @Override
    public void renderImage(Font textRenderer, int x, int y, GuiGraphics context) {
        if (this.texture == null || !TooltipReforgedConfig.INSTANCE.tooltip.bannerTooltip.getValue()) return;
        context.blit(this.texture.texture().withPrefix("textures/").withSuffix(".png"), x, y, 22, 42, 0, 0, 22, 42, 64, 64);
    }

    private static Material getTexture(ItemStack stack/*? >=1.21 {*/, RegistryAccess registries/*?}*/) {
        List<Holder<BannerPattern>> patterns = getPatternsFor(stack/*? >=1.21 {*/, registries/*?}*/);
        //? if <1.21 {
        /*return patterns.stream().findFirst().map(Holder::unwrapKey).flatMap(x -> x).map(Sheets::getBannerMaterial).orElse(null);
        *///?} else {
        return patterns.stream().findFirst().map(Sheets::getBannerMaterial).orElse(null);
        //?}
    }

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
}
