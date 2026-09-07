package com.iafenvoy.tooltipsreforged.mixin;

import com.iafenvoy.tooltipsreforged.Static;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.render.TooltipsRenderHelper;
import com.iafenvoy.tooltipsreforged.util.TooltipScrollTracker;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(GuiGraphics.class)
public abstract class DrawContextMixin {
    @Unique
    private static final ThreadLocal<ItemStack> PREV_STACK = new ThreadLocal<>();

    @Inject(method = "renderTooltipInternal(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)V", at = @At("HEAD"), cancellable = true)
    private void injectDrawTooltip(Font textRenderer, List<ClientTooltipComponent> components, int x, int y, ClientTooltipPositioner positioner, CallbackInfo ci) {
        List<ClientTooltipComponent> mutable = new ArrayList<>(components);
        ItemStack stack = Static.CACHE.get();
        if (stack != null && !TooltipReforgedConfig.INSTANCE.misc.blacklist.getValue().contains(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString())) {
            if (PREV_STACK.get() != stack) TooltipScrollTracker.resetScroll();
            else TooltipScrollTracker.tick();
            PREV_STACK.set(stack);
            Static.CACHE.remove();
            TooltipsRenderHelper.render(stack, mutable, (GuiGraphics) (Object) this, textRenderer, x, y, positioner);
            ci.cancel();
        }
    }
}

