package com.iafenvoy.tooltipsreforged.util;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
//? if <1.21 {
/*import net.minecraft.world.item.alchemy.PotionUtils;
*///?} else {
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.alchemy.PotionContents;
//?}

import java.util.ArrayList;
import java.util.List;

/**
 * Shared entry points that hide the small API differences between 1.20.1 and 1.21+,
 * so render components only deal with version-neutral data.
 */
public final class EffectsHelper {
    public static List<MobEffectInstance> getPotionEffects(ItemStack stack) {
        //? if <1.21 {
        /*return PotionUtils.getMobEffects(stack);
        *///?} else {
        List<MobEffectInstance> effects = new ArrayList<>();
        stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).forEachEffect(effects::add);
        return effects;
        //?}
    }

    public static int getEffectColor(MobEffectInstance effect) {
        //? if <1.21 {
        /*return effect.getEffect().getColor();
        *///?} else {
        return effect.getEffect().value().getColor();
        //?}
    }

    public static FoodProperties getFoodProperties(ItemStack stack) {
        //? if <1.21 {
        /*return stack.getItem().getFoodProperties();
        *///?} else {
        return stack.getOrDefault(DataComponents.FOOD, Foods.APPLE);
        //?}
    }

    public static int getNutrition(FoodProperties food) {
        //? if <1.21 {
        /*return food.getNutrition();
        *///?} else {
        return food.nutrition();
        //?}
    }

    public static float getSaturationIncrement(FoodProperties food) {
        //? if <1.21 {
        /*return food.getNutrition() * food.getSaturationModifier() * 2.0f;
        *///?} else {
        return food.saturation();
        //?}
    }

    public static List<MobEffectInstance> getFoodEffects(FoodProperties food) {
        //? if <1.21 {
        /*return food.getEffects().stream().map(pair -> pair.getFirst()).toList();
        *///?} else {
        return food.effects().stream().map(FoodProperties.PossibleEffect::effect).toList();
        //?}
    }
}
