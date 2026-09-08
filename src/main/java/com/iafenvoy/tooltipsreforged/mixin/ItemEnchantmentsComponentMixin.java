package com.iafenvoy.tooltipsreforged.mixin;

//? if >=1.21 {
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEnchantments.class)
public class ItemEnchantmentsComponentMixin {
    @Inject(method = "addToTooltip", at = @At("HEAD"), cancellable = true)
    private static void cancelVanillaEnchantmentTooltips(CallbackInfo ci) {
        if (TooltipReforgedConfig.INSTANCE.tooltip.enchantmentTooltip.getValue().shouldRender())
            ci.cancel();
    }
}

//?}
