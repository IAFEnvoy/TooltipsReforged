package com.iafenvoy.tooltipsreforged.util;

//? if <1.21 {
import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import it.unimi.dsi.fastutil.objects.ObjectLongImmutablePair;
import it.unimi.dsi.fastutil.objects.ObjectLongPair;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class InfoCollectHelper {
    private static final List<Attribute> ATTRIBUTES = List.of(Attributes.MAX_HEALTH, Attributes.MOVEMENT_SPEED, Attributes.ATTACK_DAMAGE, Attributes.ARMOR, Attributes.FOLLOW_RANGE, Attributes.KNOCKBACK_RESISTANCE, Attributes.ATTACK_KNOCKBACK);
    private static final Map<EnchantmentCategory, List<Item>> TARGET_ITEMS_MAP = new HashMap<>();

    public static List<String> collectItemTags(ItemStack stack) {
        return BuiltInRegistries.ITEM.wrapAsHolder(stack.getItem()).tags().map(TagKey::location).map(x -> "#" + x.toString()).toList();
    }

    public static List<String> collectBlockTags(ItemStack stack) {
        if (stack.getItem() instanceof BlockItem blockItem)
            return BuiltInRegistries.BLOCK.wrapAsHolder(blockItem.getBlock()).tags().map(TagKey::location).map(x -> "#" + x.toString()).toList();
        else return List.of();
    }

    public static List<String> collectEntityTags(ItemStack stack) {
        if (stack.getItem() instanceof SpawnEggItem eggItem)
            return BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(eggItem.getType(stack.getTag())).tags().map(TagKey::location).map(x -> "#" + x.toString()).toList();
        else return List.of();
    }

    public static List<MutableComponent> collectEntityInfo(ItemStack stack) {
        List<MutableComponent> list = new LinkedList<>();
        Level world = Minecraft.getInstance().level;
        if (stack.getItem() instanceof SpawnEggItem eggItem && world != null && eggItem.getType(stack.getTag()).create(world) instanceof LivingEntity living) {
            DecimalFormat df = new DecimalFormat("#.##");
            ATTRIBUTES.forEach(attribute -> {
                AttributeInstance instance = living.getAttribute(attribute);
                if (instance != null)
                    list.add(Component.translatable(attribute.getDescriptionId()).append(": ").append(df.format(instance.getValue())).withStyle(ChatFormatting.GRAY));
            });
            list.add(Component.translatable("text.tooltips_reforged.mob_type").append(Component.translatable(getMobType(living.getMobType()))).withStyle(ChatFormatting.GRAY));
        }
        return list;
    }

    public static List<MutableComponent> collectNbt(ItemStack stack) {
        return NbtProcessor.process(stack);
    }

    private static String getMobType(MobType type) {
        if (type == MobType.UNDEFINED) return "type.tooltips_reforged.default";
        if (type == MobType.UNDEAD) return "type.tooltips_reforged.undead";
        if (type == MobType.ARTHROPOD) return "type.tooltips_reforged.arthropod";
        if (type == MobType.ILLAGER) return "type.tooltips_reforged.illager";
        if (type == MobType.WATER) return "type.tooltips_reforged.aquatic";
        return "";
    }

    @Nullable
    public static ObjectLongPair<ResourceLocation> collectLootTable(ItemStack stack) {
        CompoundTag nbt = BlockItem.getBlockEntityData(stack);
        if (nbt != null && nbt.contains("LootTable", 8))
            return new ObjectLongImmutablePair<>(ResourceLocation.tryParse(nbt.getString("LootTable")), nbt.getLong("LootTableSeed"));
        else return null;
    }

    public static List<Item> getEnchantmentTarget(EnchantmentCategory target) {
        return TARGET_ITEMS_MAP.getOrDefault(target, List.of());
    }

    @Nullable
    public static NonNullList<ItemStack> collectContainer(ItemStack stack) {
        NonNullList<ItemStack> stacks = NonNullList.withSize(27, ItemStack.EMPTY);
        CompoundTag nbt = BlockItem.getBlockEntityData(stack);
        if (nbt == null || !nbt.contains("Items", Tag.TAG_LIST)) return null;
        ContainerHelper.loadAllItems(nbt, stacks);
        return stacks;
    }

    static {
        for (EnchantmentCategory target : EnchantmentCategory.values())
            try {
                TARGET_ITEMS_MAP.put(target, BuiltInRegistries.ITEM.stream().filter(target::canEnchant).toList());
            } catch (Throwable e) {
                TooltipReforgedClient.LOGGER.error("Failed to load items for enchantment target {}", target.name(), e);
            }
    }
}

//?} else {
import it.unimi.dsi.fastutil.objects.ObjectLongImmutablePair;
import it.unimi.dsi.fastutil.objects.ObjectLongPair;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

public final class InfoCollectHelper {
    private static final List<Holder<Attribute>> ATTRIBUTES = List.of(Attributes.MAX_HEALTH, Attributes.MOVEMENT_SPEED, Attributes.ATTACK_DAMAGE, Attributes.ARMOR, Attributes.FOLLOW_RANGE, Attributes.KNOCKBACK_RESISTANCE, Attributes.ATTACK_KNOCKBACK);

    public static List<String> collectItemTags(ItemStack stack) {
        return BuiltInRegistries.ITEM.wrapAsHolder(stack.getItem()).tags().map(TagKey::location).map(x -> "#" + x.toString()).toList();
    }

    public static List<String> collectBlockTags(ItemStack stack) {
        if (stack.getItem() instanceof BlockItem blockItem)
            return BuiltInRegistries.BLOCK.wrapAsHolder(blockItem.getBlock()).tags().map(TagKey::location).map(x -> "#" + x.toString()).toList();
        else return List.of();
    }

    public static List<String> collectEntityTags(ItemStack stack) {
        if (stack.getItem() instanceof SpawnEggItem eggItem)
            return BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(eggItem.getType(stack)).tags().map(TagKey::location).map(x -> "#" + x.toString()).toList();
        else return List.of();
    }

    public static List<MutableComponent> collectEntityInfo(ItemStack stack) {
        List<MutableComponent> list = new LinkedList<>();
        Level world = Minecraft.getInstance().level;
        if (stack.getItem() instanceof SpawnEggItem eggItem && world != null && eggItem.getType(stack).create(world) instanceof LivingEntity living) {
            DecimalFormat df = new DecimalFormat("#.##");
            ATTRIBUTES.forEach(attribute -> {
                AttributeInstance instance = living.getAttribute(attribute);
                if (instance != null)
                    list.add(Component.translatable(attribute.value().getDescriptionId()).append(": ").append(df.format(instance.getValue())).withStyle(ChatFormatting.GRAY));
            });
            list.add(Component.translatable("text.tooltips_reforged.mob_type").append(Component.translatable(getMobType(living.getType()))).withStyle(ChatFormatting.GRAY));
        }
        return list;
    }

    public static List<MutableComponent> collectNbt(ItemStack stack, RegistryAccess registries) {
        return ComponentsProcessor.processStack(stack, registries);
    }

    private static String getMobType(EntityType<?> type) {
        if (type.is(EntityTypeTags.UNDEAD)) return "type.tooltips_reforged.undead";
        if (type.is(EntityTypeTags.ARTHROPOD)) return "type.tooltips_reforged.arthropod";
        if (type.is(EntityTypeTags.ILLAGER)) return "type.tooltips_reforged.illager";
        if (type.is(EntityTypeTags.AQUATIC)) return "type.tooltips_reforged.aquatic";
        return "type.tooltips_reforged.default";
    }

    @Nullable
    public static ObjectLongPair<ResourceLocation> collectLootTable(ItemStack stack) {
        CompoundTag nbt = stack.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY).copyTag();
        if (nbt != null && nbt.contains("LootTable", 8))
            return new ObjectLongImmutablePair<>(ResourceLocation.tryParse(nbt.getString("LootTable")), nbt.getLong("LootTableSeed"));
        else return null;
    }

    @Nullable
    public static NonNullList<ItemStack> collectContainer(ItemStack stack, HolderLookup.Provider registries) {
        NonNullList<ItemStack> stacks = NonNullList.withSize(27, ItemStack.EMPTY);
        CompoundTag nbt = stack.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY).copyTag();
        if (nbt == null || !nbt.contains("Items", Tag.TAG_LIST)) return null;
        ContainerHelper.loadAllItems(nbt, stacks, registries);
        return stacks;
    }
}

//?}
