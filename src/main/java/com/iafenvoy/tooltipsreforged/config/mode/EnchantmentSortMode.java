package com.iafenvoy.tooltipsreforged.config.mode;

//? if <1.21 {
import com.iafenvoy.tooltipsreforged.component.EnchantmentsComponent;

import java.util.Comparator;

@SuppressWarnings("unused")
public enum EnchantmentSortMode {
    DEFAULT(Comparator.comparing(e -> 0)),
    LEVEL(Comparator.comparingInt(EnchantmentsComponent.EnchantmentInfo::level).reversed()),
    NAME(Comparator.comparing(EnchantmentsComponent.EnchantmentInfo::id)),
    TYPE(Comparator.comparingInt(e -> e.enchantment().isCurse() ? 1 : 0));
    private final Comparator<EnchantmentsComponent.EnchantmentInfo> comparator;

    EnchantmentSortMode(Comparator<EnchantmentsComponent.EnchantmentInfo> comparator) {
        this.comparator = comparator;
    }

    public Comparator<EnchantmentsComponent.EnchantmentInfo> getComparator() {
        return this.comparator;
    }
}


//?} else {
import com.iafenvoy.tooltipsreforged.component.EnchantmentsComponent;
import java.util.Comparator;
import net.minecraft.tags.EnchantmentTags;

@SuppressWarnings("unused")
public enum EnchantmentSortMode {
    DEFAULT(Comparator.comparing(e -> 0)),
    LEVEL(Comparator.comparingInt(EnchantmentsComponent.EnchantmentInfo::level).reversed()),
    NAME(Comparator.comparing(EnchantmentsComponent.EnchantmentInfo::id)),
    TYPE(Comparator.comparingInt(e -> e.enchantment().is(EnchantmentTags.CURSE) ? 1 : 0));
    private final Comparator<EnchantmentsComponent.EnchantmentInfo> comparator;

    EnchantmentSortMode(Comparator<EnchantmentsComponent.EnchantmentInfo> comparator) {
        this.comparator = comparator;
    }

    public Comparator<EnchantmentsComponent.EnchantmentInfo> getComparator() {
        return this.comparator;
    }
}


//?}
