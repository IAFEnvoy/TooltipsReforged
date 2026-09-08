package com.iafenvoy.tooltipsreforged.mixin;

//? forge {
/*import com.iafenvoy.tooltipsreforged.component.EntityViewerComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
@Mixin(MobBucketItem.class)
public class EntityBucketItemForgeMixin {
    @Inject(method = "<init>(Ljava/util/function/Supplier;Ljava/util/function/Supplier;Ljava/util/function/Supplier;Lnet/minecraft/world/item/Item$Properties;)V", at = @At("RETURN"))
    private void saveEntityType(Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier, Item.Properties properties, CallbackInfo ci) {
        EntityViewerComponent.ENTITY_BUCKET_MAP.put((MobBucketItem) (Object) this, entitySupplier);
    }
}
*/
