package com.iafenvoy.tooltipsreforged.mixin;

import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShulkerBoxBlock.class)
public class ShulkerBoxBlockMixin {
    @Inject(method = "appendHoverText", at = @At("HEAD"), cancellable = true)
    private void wrapTooltip(CallbackInfo ci) {
        if (TooltipReforgedConfig.INSTANCE.tooltip.containerTooltip.getValue().shouldRenderImage())
            ci.cancel();
    }
}

