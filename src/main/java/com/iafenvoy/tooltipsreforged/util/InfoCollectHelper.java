package com.iafenvoy.tooltipsreforged.util;

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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
//? if <1.21 {
/*import net.minecraft.world.entity.MobType;
*///?} else {
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.item.component.CustomData;
//?}

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

public final class InfoCollectHelper {
    //? if <1.21 {
    /*private static final List<Attribute> ATTRIBUTES = List.of(Attributes.MAX_HEALTH, Attributes.MOVEMENT_SPEED, Attributes.ATTACK_DAMAGE, Attributes.ARMOR, Attributes.FOLLOW_RANGE, Attributes.KNOCKBACK_RESISTANCE, Attributes.ATTACK_KNOCKBACK);
    *///?} else {
    private static final List<Holder<Attribute>> ATTRIBUTES = List.of(Attributes.MAX_HEALTH, Attributes.MOVEMENT_SPEED, Attributes.ATTACK_DAMAGE, Attributes.ARMOR, Attributes.FOLLOW_RANGE, Attributes.KNOCKBACK_RESISTANCE, Attributes.ATTACK_KNOCKBACK);
    //?}

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
            return BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(getSpawnEggType(eggItem, stack)).tags().map(TagKey::location).map(x -> "#" + x.toString()).toList();
        else return List.of();
    }

    public static List<MutableComponent> collectEntityInfo(ItemStack stack) {
        List<MutableComponent> list = new LinkedList<>();
        Level world = Minecraft.getInstance().level;
        if (stack.getItem() instanceof SpawnEggItem eggItem && world != null && getSpawnEggType(eggItem, stack).create(world) instanceof LivingEntity living) {
            DecimalFormat df = new DecimalFormat("#.##");
            ATTRIBUTES.forEach(attribute -> {
                AttributeInstance instance = living.getAttribute(attribute);
                if (instance != null)
                    list.add(Component.translatable(getAttributeName(attribute)).append(": ").append(df.format(instance.getValue())).withStyle(ChatFormatting.GRAY));
            });
            list.add(Component.translatable("text.tooltips_reforged.mob_type").append(Component.translatable(getMobTypeKey(living))).withStyle(ChatFormatting.GRAY));
        }
        return list;
    }

    private static EntityType<?> getSpawnEggType(SpawnEggItem eggItem, ItemStack stack) {
        //? if <1.21 {
        /*return eggItem.getType(stack.getTag());
        *///?} else {
        return eggItem.getType(stack);
        //?}
    }

    //? if <1.21 {
    /*private static String getAttributeName(Attribute attribute) {
        return attribute.getDescriptionId();
    }

    private static String getMobTypeKey(LivingEntity living) {
        MobType type = living.getMobType();
        if (type == MobType.UNDEFINED) return "type.tooltips_reforged.default";
        if (type == MobType.UNDEAD) return "type.tooltips_reforged.undead";
        if (type == MobType.ARTHROPOD) return "type.tooltips_reforged.arthropod";
        if (type == MobType.ILLAGER) return "type.tooltips_reforged.illager";
        if (type == MobType.WATER) return "type.tooltips_reforged.aquatic";
        return "";
    }
    *///?} else {
    private static String getAttributeName(Holder<Attribute> attribute) {
        return attribute.value().getDescriptionId();
    }

    private static String getMobTypeKey(LivingEntity living) {
        EntityType<?> type = living.getType();
        if (type.is(EntityTypeTags.UNDEAD)) return "type.tooltips_reforged.undead";
        if (type.is(EntityTypeTags.ARTHROPOD)) return "type.tooltips_reforged.arthropod";
        if (type.is(EntityTypeTags.ILLAGER)) return "type.tooltips_reforged.illager";
        if (type.is(EntityTypeTags.AQUATIC)) return "type.tooltips_reforged.aquatic";
        return "type.tooltips_reforged.default";
    }
    //?}

    public static List<MutableComponent> collectNbt(ItemStack stack/*? >=1.21 {*/, RegistryAccess registries/*?}*/) {
        //? if <1.21 {
        /*return NbtProcessor.process(stack);
        *///?} else {
        return ComponentsProcessor.processStack(stack, registries);
        //?}
    }

    @Nullable
    public static ObjectLongPair<ResourceLocation> collectLootTable(ItemStack stack) {
        //? if <1.21 {
        /*CompoundTag nbt = BlockItem.getBlockEntityData(stack);
        *///?} else {
        CompoundTag nbt = stack.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY).copyTag();
        //?}
        if (nbt != null && nbt.contains("LootTable", 8))
            return new ObjectLongImmutablePair<>(ResourceLocation.tryParse(nbt.getString("LootTable")), nbt.getLong("LootTableSeed"));
        else return null;
    }

    @Nullable
    public static NonNullList<ItemStack> collectContainer(ItemStack stack/*? >=1.21 {*/, HolderLookup.Provider registries/*?}*/) {
        NonNullList<ItemStack> stacks = NonNullList.withSize(27, ItemStack.EMPTY);
        //? if <1.21 {
        /*CompoundTag nbt = BlockItem.getBlockEntityData(stack);
        *///?} else {
        CompoundTag nbt = stack.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY).copyTag();
        //?}
        if (nbt == null || !nbt.contains("Items", Tag.TAG_LIST)) return null;
        //? if <1.21 {
        /*ContainerHelper.loadAllItems(nbt, stacks);
        *///?} else {
        ContainerHelper.loadAllItems(nbt, stacks, registries);
        //?}
        return stacks;
    }
}
