package com.iafenvoy.tooltipsreforged.config.mode;

import com.iafenvoy.tooltipsreforged.component.EnchantmentsComponent;

import java.util.Comparator;
//? >=1.21 {
import net.minecraft.tags.EnchantmentTags;
//?}

@SuppressWarnings("unused")
public enum EnchantmentSortMode {
    DEFAULT(Comparator.comparing(e -> 0)),
    LEVEL(Comparator.comparingInt(EnchantmentsComponent.EnchantmentInfo::level).reversed()),
    NAME(Comparator.comparing(EnchantmentsComponent.EnchantmentInfo::id)),
    TYPE(Comparator.comparingInt(EnchantmentSortMode::typeWeight));
    private final Comparator<EnchantmentsComponent.EnchantmentInfo> comparator;

    EnchantmentSortMode(Comparator<EnchantmentsComponent.EnchantmentInfo> comparator) {
        this.comparator = comparator;
    }

    public Comparator<EnchantmentsComponent.EnchantmentInfo> getComparator() {
        return this.comparator;
    }

    private static int typeWeight(EnchantmentsComponent.EnchantmentInfo e) {
        //? if <1.21 {
        /*return e.enchantment().isCurse() ? 1 : 0;
        *///?} else {
        return e.enchantment().is(EnchantmentTags.CURSE) ? 1 : 0;
        //?}
    }
}


