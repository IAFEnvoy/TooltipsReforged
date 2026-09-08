package com.iafenvoy.tooltipsreforged.hook;

import org.jetbrains.annotations.ApiStatus;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Rarity;

//Forge extended Rarity class, so we need this.
public final class RarityHook {
    private static final List<BiFunction<MutableComponent, Rarity, MutableComponent>> HOOKS = new LinkedList<>();

    public static void register(BiFunction<MutableComponent, Rarity, MutableComponent> hook) {
        HOOKS.add(hook);
    }

    @ApiStatus.Internal
    public static MutableComponent applyColor(MutableComponent text, Rarity rarity) {
        return HOOKS.stream().collect(() -> text, (p, c) -> c.apply(p, rarity), MutableComponent::append);
    }

    static {
        register((text, rarity) -> text.withStyle(rarity.color/*? >=1.21 {*/()/*?}*/ == ChatFormatting.BLACK ? ChatFormatting.WHITE : rarity.color/*? >=1.21 {*/()/*?}*/));
    }
}

