package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.render.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.Pufferfish;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.SpawnEggItem;
//? >=1.21 {
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
//?}
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class EntityViewerComponent extends StandaloneComponent implements RenderHelper {
    public static final Map<MobBucketItem, Supplier<? extends EntityType<?>>> ENTITY_BUCKET_MAP = new HashMap<>();
    private static final float ROTATION_INCREMENT = 0.2f;
    private static float CURRENT_ROTATION = 0f;
    private static final int ENTITY_OFFSET = 40;

    public EntityViewerComponent(ItemStack stack) {
        super(stack);
    }

    @Override
    public void render(GuiGraphics context, Font textRenderer, int x, int y, int tooltipWidth, int z) {
        CURRENT_ROTATION = (CURRENT_ROTATION + ROTATION_INCREMENT) % 360;
        if (this.stack.getItem() instanceof MobBucketItem bucketItem) {
            if (TooltipReforgedConfig.INSTANCE.tooltip.bucketTooltip.getValue())
                this.renderBucketEntity(context, x, y, tooltipWidth, z, bucketItem);
        } else if (this.stack.getItem() instanceof SpawnEggItem spawnEggItem) {
            if (TooltipReforgedConfig.INSTANCE.tooltip.spawnEggTooltip.getValue())
                this.renderSpawnEggEntity(context, x, y, tooltipWidth, z, spawnEggItem);
        }
    }

    private void renderBucketEntity(GuiGraphics context, int x, int y, int tooltipWidth, int z, MobBucketItem bucketItem) {
        if (!ENTITY_BUCKET_MAP.containsKey(bucketItem)) return;
        EntityType<?> entityType = ENTITY_BUCKET_MAP.get(bucketItem).get();
        Entity entity = entityType.create(Minecraft.getInstance().level);

        if (entity instanceof Bucketable bucketable && entity instanceof LivingEntity livingEntity) {
            //? if <1.21 {
            /*CompoundTag nbtComponent = this.stack.getOrCreateTag();
            *///?} else {
            CompoundTag nbtComponent = this.stack.getOrDefault(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY).copyTag();
            //?}
            bucketable.loadFromBucketTag(nbtComponent);
            if (entityType == EntityType.TROPICAL_FISH) return;
            if (bucketable instanceof Pufferfish pufferfishEntity) pufferfishEntity.setPuffState(2);

            int frameX = this.getFrameX(context, x, tooltipWidth, 80, -ENTITY_OFFSET - 70);
            this.renderBackground(context, frameX, y, 80, 40, z);
            livingEntity.tick();
            this.drawEntity(context, frameX + 43, y + 40, 30, -CURRENT_ROTATION, livingEntity);
        }
    }

    private void renderSpawnEggEntity(GuiGraphics context, int x, int y, int tooltipWidth, int z, SpawnEggItem spawnEggItem) {
        //? if <1.21 {
        /*EntityType<?> entityType = spawnEggItem.getType(this.stack.getTag());
        *///?} else {
        EntityType<?> entityType = spawnEggItem.getType(this.stack);
        //?}
        Entity entity = entityType.create(Minecraft.getInstance().level);
        if (entity == null) return;

        if (entityType == EntityType.VILLAGER || entityType == EntityType.ZOMBIE_VILLAGER) {
            CompoundTag villagerData = new CompoundTag();
            villagerData.putString("profession", "minecraft:none");
            villagerData.putString("type", "minecraft:plains");
            CompoundTag nbt = new CompoundTag();
            nbt.put("VillagerData", villagerData);
            entity.load(nbt);
        }

        if (entityType == EntityType.TROPICAL_FISH) return;
        if (entity instanceof Pufferfish pufferfishEntity) pufferfishEntity.setPuffState(2);
        if (entity instanceof SnowGolem snowGolemEntity) snowGolemEntity.setPumpkin(false);

        if (entity instanceof LivingEntity livingEntity) {
            //? if <1.21 {
            /*CompoundTag nbt = this.stack.getTagElement("EntityTag");
            *///?} else {
            CompoundTag nbt = this.stack.getOrDefault(DataComponents.ENTITY_DATA, CustomData.EMPTY).copyTag();
            //?}
            if (nbt != null) livingEntity.readAdditionalSaveData(nbt);
            int frameX = this.getFrameX(context, x, tooltipWidth, 80, -ENTITY_OFFSET - 70);
            this.renderBackground(context, frameX, y, 80, 80, z);
            livingEntity.tick();
            this.drawEntity(context, frameX + 43, y + 75, 40, -CURRENT_ROTATION, livingEntity);
        }
    }
}
