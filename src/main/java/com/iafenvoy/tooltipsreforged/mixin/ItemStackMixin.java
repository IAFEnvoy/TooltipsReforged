package com.iafenvoy.tooltipsreforged.mixin;

//? if <1.21 {
import com.iafenvoy.tooltipsreforged.Static;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.config.mode.EnchantmentsRenderMode;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin({ItemStack.class})
public abstract class ItemStackMixin {
    @Inject(method = "getTooltipLines", at = @At("RETURN"))
    private void onGetTooltip(Player player, TooltipFlag context, CallbackInfoReturnable<List<Component>> cir) {
        Static.CACHE.set((ItemStack) (Object) this);
    }

    @Inject(method = "appendEnchantmentNames", at = @At("HEAD"), cancellable = true)
    private static void cancelVanillaEnchantmentTooltips(CallbackInfo ci) {
        if (TooltipReforgedConfig.INSTANCE.tooltip.enchantmentTooltip.getValue().shouldRender())
            ci.cancel();
    }
}
//?} else {
import com.iafenvoy.tooltipsreforged.Static;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin({ItemStack.class})
public abstract class ItemStackMixin {
    @Inject(method = "getTooltipLines", at = @At("RETURN"))
    private void onGetTooltip(Item.TooltipContext context, Player player, TooltipFlag type, CallbackInfoReturnable<List<Component>> cir) {
        Static.CACHE.set((ItemStack) (Object) this);
    }
}
//?}
