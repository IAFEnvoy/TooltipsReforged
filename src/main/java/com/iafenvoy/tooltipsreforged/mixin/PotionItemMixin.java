package com.iafenvoy.tooltipsreforged.mixin;

import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import net.minecraft.world.item.PotionItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PotionItem.class)
public class PotionItemMixin {
    @Inject(method = "appendHoverText", at = @At("HEAD"), cancellable = true)
    private void wrapTooltip(CallbackInfo ci) {
        if (TooltipReforgedConfig.INSTANCE.tooltip.effectsTooltip.getValue().shouldRender())
            ci.cancel();
    }
}
