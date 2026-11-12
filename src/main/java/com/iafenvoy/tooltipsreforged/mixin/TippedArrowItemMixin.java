package com.iafenvoy.tooltipsreforged.mixin;

import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import net.minecraft.world.item.TippedArrowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TippedArrowItem.class)
public class TippedArrowItemMixin {
    @Inject(method = "appendHoverText", at = @At("HEAD"), cancellable = true)
    private void wrapTooltip(CallbackInfo ci) {
        if (TooltipReforgedConfig.INSTANCE.tooltip.effectsTooltip.getValue().shouldRender())
            ci.cancel();
    }
}
