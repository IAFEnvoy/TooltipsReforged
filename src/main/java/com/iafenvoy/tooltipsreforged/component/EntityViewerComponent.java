package com.iafenvoy.tooltipsreforged.component;

//? if <1.21 {
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
    public void render(GuiGraphics context, Font textRenderer, int x, int y, int z) {
        CURRENT_ROTATION = (CURRENT_ROTATION + ROTATION_INCREMENT) % 360;
        if (this.stack.getItem() instanceof MobBucketItem bucketItem) {
            if (TooltipReforgedConfig.INSTANCE.tooltip.bucketTooltip.getValue())
                this.renderBucketEntity(context, x, y, z, bucketItem);
        } else if (this.stack.getItem() instanceof SpawnEggItem spawnEggItem) {
            if (TooltipReforgedConfig.INSTANCE.tooltip.spawnEggTooltip.getValue())
                this.renderSpawnEggEntity(context, x, y, z, spawnEggItem);
        }
    }

    private void renderBucketEntity(GuiGraphics context, int x, int y, int z, MobBucketItem bucketItem) {
        if (!ENTITY_BUCKET_MAP.containsKey(bucketItem)) return;
        EntityType<?> entityType = ENTITY_BUCKET_MAP.get(bucketItem).get();
        Entity entity = entityType.create(Minecraft.getInstance().level);

        if (entity instanceof Bucketable bucketable && entity instanceof LivingEntity livingEntity) {
            CompoundTag nbtComponent = this.stack.getOrCreateTag();
            bucketable.loadFromBucketTag(nbtComponent);
            if (entityType == EntityType.TROPICAL_FISH) return;
            if (bucketable instanceof Pufferfish pufferfishEntity) pufferfishEntity.setPuffState(2);

            this.renderBackground(context, x - ENTITY_OFFSET - 70, y, 80, 40, z);
            livingEntity.tick();
            this.drawEntity(context, x - 67, y + 40, 30, -CURRENT_ROTATION, livingEntity);
        }
    }

    private void renderSpawnEggEntity(GuiGraphics context, int x, int y, int z, SpawnEggItem spawnEggItem) {
        EntityType<?> entityType = spawnEggItem.getType(this.stack.getTag());
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
            CompoundTag nbt = this.stack.getTagElement("EntityTag");
            if (nbt != null) livingEntity.readAdditionalSaveData(nbt);
            this.renderBackground(context, x - ENTITY_OFFSET - 70, y, 80, 80, z);
            livingEntity.tick();
            this.drawEntity(context, x - 67, y + 75, 40, -CURRENT_ROTATION, livingEntity);
        }
    }
}

//?} else {
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.render.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.component.DataComponents;
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
import net.minecraft.world.item.component.CustomData;
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
    public void render(GuiGraphics context, Font textRenderer, int x, int y, int z) {
        CURRENT_ROTATION = (CURRENT_ROTATION + ROTATION_INCREMENT) % 360;
        if (this.stack.getItem() instanceof MobBucketItem bucketItem) {
            if (TooltipReforgedConfig.INSTANCE.tooltip.bucketTooltip.getValue())
                this.renderBucketEntity(context, x, y, z, bucketItem);
        } else if (this.stack.getItem() instanceof SpawnEggItem spawnEggItem) {
            if (TooltipReforgedConfig.INSTANCE.tooltip.spawnEggTooltip.getValue())
                this.renderSpawnEggEntity(context, x, y, z, spawnEggItem);
        }
    }

    private void renderBucketEntity(GuiGraphics context, int x, int y, int z, MobBucketItem bucketItem) {
        if (!ENTITY_BUCKET_MAP.containsKey(bucketItem)) return;
        EntityType<?> entityType = ENTITY_BUCKET_MAP.get(bucketItem).get();
        Entity entity = entityType.create(Minecraft.getInstance().level);

        if (entity instanceof Bucketable bucketable && entity instanceof LivingEntity livingEntity) {
            CompoundTag nbtComponent = this.stack.getOrDefault(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY).copyTag();
            bucketable.loadFromBucketTag(nbtComponent);
            if (entityType == EntityType.TROPICAL_FISH) return;
            if (bucketable instanceof Pufferfish pufferfishEntity) pufferfishEntity.setPuffState(2);

            this.renderBackground(context, x - ENTITY_OFFSET - 70, y, 80, 40, z);
            livingEntity.tick();
            this.drawEntity(context, x - 67, y + 40, 30, -CURRENT_ROTATION, livingEntity);
        }
    }

    private void renderSpawnEggEntity(GuiGraphics context, int x, int y, int z, SpawnEggItem spawnEggItem) {
        EntityType<?> entityType = spawnEggItem.getType(this.stack);
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
            CompoundTag nbt = this.stack.getOrDefault(DataComponents.ENTITY_DATA, CustomData.EMPTY).copyTag();
            if (nbt != null) livingEntity.readAdditionalSaveData(nbt);
            this.renderBackground(context, x - ENTITY_OFFSET - 70, y, 80, 80, z);
            livingEntity.tick();
            this.drawEntity(context, x - 67, y + 75, 40, -CURRENT_ROTATION, livingEntity);
        }
    }
}

//?}
