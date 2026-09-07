package com.iafenvoy.tooltipsreforged.mixin;

import com.iafenvoy.tooltipsreforged.component.EntityViewerComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobBucketItem.class)
public class EntityBucketItemMixin {
    @Inject(method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/material/Fluid;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/world/item/Item$Properties;)V", at = @At("RETURN"))
    private void saveEntityType(EntityType<?> type, Fluid fluid, SoundEvent emptyingSound, Item.Properties settings, CallbackInfo ci) {
        EntityViewerComponent.ENTITY_BUCKET_MAP.put((MobBucketItem) (Object) this, () -> type);
    }
}

