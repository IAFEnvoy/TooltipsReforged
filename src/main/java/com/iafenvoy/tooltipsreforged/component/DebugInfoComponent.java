package com.iafenvoy.tooltipsreforged.component;

//? if <1.21 {
import com.google.common.collect.ImmutableList;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.util.InfoCollectHelper;
import com.iafenvoy.tooltipsreforged.util.TooltipKeyManager;
import it.unimi.dsi.fastutil.objects.ObjectLongPair;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.List;

public class DebugInfoComponent implements ClientTooltipComponent {
    private static final TooltipKeyManager KEY_MANAGER = new TooltipKeyManager();
    private final List<String> itemTags, blockTags, entityTags;
    private final List<MutableComponent> nbt, entityInfo;
    private final ObjectLongPair<ResourceLocation> lootTable;

    public DebugInfoComponent(ItemStack stack) {
        this.itemTags = InfoCollectHelper.collectItemTags(stack);
        this.blockTags = InfoCollectHelper.collectBlockTags(stack);
        this.entityTags = InfoCollectHelper.collectEntityTags(stack);
        this.nbt = InfoCollectHelper.collectNbt(stack);
        this.entityInfo = InfoCollectHelper.collectEntityInfo(stack);
        this.lootTable = InfoCollectHelper.collectLootTable(stack);
    }

    @Override
    public int getHeight() {
        return TooltipReforgedConfig.INSTANCE.tooltip.debugInfoTooltip.getValue() ? this.getDisplayTexts().size() * 10 : 0;
    }

    @Override
    public int getWidth(Font textRenderer) {
        return this.getDisplayTexts().stream().map(textRenderer::width).reduce(0, Math::max, Math::max);
    }

    @Override
    public void renderText(Font textRenderer, int x, int y, Matrix4f matrix, MultiBufferSource.BufferSource vertexConsumers) {
        if (!TooltipReforgedConfig.INSTANCE.tooltip.debugInfoTooltip.getValue()) return;
        float currentY = y + 1;
        for (Component text : this.getDisplayTexts()) {
            textRenderer.drawInBatch(text, x, currentY, -1, true, matrix, vertexConsumers, Font.DisplayMode.NORMAL, 0, 0xF000F0);
            currentY += 10;
        }
        KEY_MANAGER.renderTick();
    }

    private List<MutableComponent> getDisplayTexts() {
        MutableComponent first = Component.empty();
        boolean hasInfo = false;
        List<MutableComponent> infos = List.of();
        Tuple<String, List<MutableComponent>> itemTagInfo = this.getItemTagInfo();
        TooltipKeyManager.PressState itemTagState = KEY_MANAGER.itemTag();
        if (itemTagInfo != null && itemTagState.show()) {
            hasInfo = true;
            if (itemTagState.showDetail()) {
                first.append(Component.literal("[%s %s] ".formatted(TooltipKeyManager.itemTagKeyTranslation(), I18n.get(itemTagInfo.getA()))).withStyle(ChatFormatting.WHITE));
                infos = itemTagInfo.getB().stream().map(x -> x.withStyle(ChatFormatting.DARK_GRAY)).toList();
            } else
                first.append(Component.literal("[%s %s] ".formatted(TooltipKeyManager.itemTagKeyTranslation(), I18n.get(itemTagInfo.getA()))).withStyle(ChatFormatting.GRAY));
        }
        Tuple<String, List<MutableComponent>> specificInfo = this.getSpecificInfo();
        TooltipKeyManager.PressState specificState = KEY_MANAGER.specific();
        if (specificInfo != null && specificState.show()) {
            hasInfo = true;
            if (specificState.showDetail()) {
                first.append(Component.literal("[%s %s] ".formatted(TooltipKeyManager.specificKeyTranslation(), I18n.get(specificInfo.getA()))).withStyle(ChatFormatting.WHITE));
                infos = specificInfo.getB().stream().map(x -> x.withStyle(ChatFormatting.DARK_GRAY)).toList();
            } else
                first.append(Component.literal("[%s %s] ".formatted(TooltipKeyManager.specificKeyTranslation(), I18n.get(specificInfo.getA()))).withStyle(ChatFormatting.GRAY));
        }
        Tuple<String, List<MutableComponent>> nbtInfo = this.getNbtInfo();
        TooltipKeyManager.PressState nbtState = KEY_MANAGER.nbt();
        if (nbtInfo != null && nbtState.show()) {
            hasInfo = true;
            if (nbtState.showDetail()) {
                first.append(Component.literal("[%s %s] ".formatted(TooltipKeyManager.nbtKeyTranslation(), I18n.get(nbtInfo.getA()))).withStyle(ChatFormatting.WHITE));
                infos = nbtInfo.getB().stream().map(x -> x.withStyle(ChatFormatting.DARK_GRAY)).toList();
            } else
                first.append(Component.literal("[%s %s] ".formatted(TooltipKeyManager.nbtKeyTranslation(), I18n.get(nbtInfo.getA()))).withStyle(ChatFormatting.GRAY));
        }
        return hasInfo ? ImmutableList.<MutableComponent>builder().add(first).addAll(infos).build() : List.of();
    }

    @Nullable
    private Tuple<String, List<MutableComponent>> getItemTagInfo() {
        if (!this.itemTags.isEmpty())
            return new Tuple<>("tooltip.tooltips_reforged.item_tags", this.itemTags.stream().map(Component::literal).toList());
        return null;
    }

    @Nullable
    private Tuple<String, List<MutableComponent>> getSpecificInfo() {
        if (!this.blockTags.isEmpty())
            return new Tuple<>("tooltip.tooltips_reforged.block_tags", this.blockTags.stream().map(Component::literal).toList());
        if (!this.entityTags.isEmpty())
            return new Tuple<>("tooltip.tooltips_reforged.entity_tags", this.entityTags.stream().map(Component::literal).toList());
        if (this.lootTable != null && this.lootTable.left() != null)
            return new Tuple<>("tooltip.tooltips_reforged.loot_table", List.of(
                    Component.literal("tooltip.tooltips_reforged.id" + this.lootTable.left().toString()),
                    Component.literal("tooltip.tooltips_reforged.seed" + this.lootTable.rightLong())
            ));
        return null;
    }

    @Nullable
    private Tuple<String, List<MutableComponent>> getNbtInfo() {
        if (!this.entityInfo.isEmpty())
            return new Tuple<>("tooltip.tooltips_reforged.mob_info", this.entityInfo);
        if (!this.nbt.isEmpty())
            return new Tuple<>("tooltip.tooltips_reforged.nbt", this.nbt);
        return null;
    }
}

//?} else {
import com.google.common.collect.ImmutableList;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.util.InfoCollectHelper;
import com.iafenvoy.tooltipsreforged.util.TooltipKeyManager;
import it.unimi.dsi.fastutil.objects.ObjectLongPair;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.List;

public class DebugInfoComponent implements ClientTooltipComponent {
    private static final TooltipKeyManager KEY_MANAGER = new TooltipKeyManager();
    private final List<String> itemTags, blockTags, entityTags;
    private final List<MutableComponent> nbt, entityInfo;
    private final ObjectLongPair<ResourceLocation> lootTable;

    public DebugInfoComponent(ItemStack stack, RegistryAccess registries) {
        this.itemTags = InfoCollectHelper.collectItemTags(stack);
        this.blockTags = InfoCollectHelper.collectBlockTags(stack);
        this.entityTags = InfoCollectHelper.collectEntityTags(stack);
        this.nbt = InfoCollectHelper.collectNbt(stack, registries);
        this.entityInfo = InfoCollectHelper.collectEntityInfo(stack);
        this.lootTable = InfoCollectHelper.collectLootTable(stack);
    }

    @Override
    public int getHeight() {
        return TooltipReforgedConfig.INSTANCE.tooltip.debugInfoTooltip.getValue() ? this.getDisplayTexts().size() * 10 : 0;
    }

    @Override
    public int getWidth(Font textRenderer) {
        return this.getDisplayTexts().stream().map(textRenderer::width).reduce(0, Math::max, Math::max);
    }

    @Override
    public void renderText(Font textRenderer, int x, int y, Matrix4f matrix, MultiBufferSource.BufferSource vertexConsumers) {
        if (!TooltipReforgedConfig.INSTANCE.tooltip.debugInfoTooltip.getValue()) return;
        float currentY = y + 1;
        for (Component text : this.getDisplayTexts()) {
            textRenderer.drawInBatch(text, x, currentY, -1, true, matrix, vertexConsumers, Font.DisplayMode.NORMAL, 0, 0xF000F0);
            currentY += 10;
        }
        KEY_MANAGER.renderTick();
    }

    private List<MutableComponent> getDisplayTexts() {
        MutableComponent first = Component.empty();
        boolean hasInfo = false;
        List<MutableComponent> infos = List.of();
        Tuple<String, List<MutableComponent>> itemTagInfo = this.getItemTagInfo();
        TooltipKeyManager.PressState itemTagState = KEY_MANAGER.itemTag();
        if (itemTagInfo != null && itemTagState.show()) {
            hasInfo = true;
            if (itemTagState.showDetail()) {
                first.append(Component.literal("[%s %s] ".formatted(TooltipKeyManager.itemTagKeyTranslation(), I18n.get(itemTagInfo.getA()))).withStyle(ChatFormatting.WHITE));
                infos = itemTagInfo.getB().stream().map(x -> x.withStyle(ChatFormatting.DARK_GRAY)).toList();
            } else
                first.append(Component.literal("[%s %s] ".formatted(TooltipKeyManager.itemTagKeyTranslation(), I18n.get(itemTagInfo.getA()))).withStyle(ChatFormatting.GRAY));
        }
        Tuple<String, List<MutableComponent>> specificInfo = this.getSpecificInfo();
        TooltipKeyManager.PressState specificState = KEY_MANAGER.specific();
        if (specificInfo != null && specificState.show()) {
            hasInfo = true;
            if (specificState.showDetail()) {
                first.append(Component.literal("[%s %s] ".formatted(TooltipKeyManager.specificKeyTranslation(), I18n.get(specificInfo.getA()))).withStyle(ChatFormatting.WHITE));
                infos = specificInfo.getB().stream().map(x -> x.withStyle(ChatFormatting.DARK_GRAY)).toList();
            } else
                first.append(Component.literal("[%s %s] ".formatted(TooltipKeyManager.specificKeyTranslation(), I18n.get(specificInfo.getA()))).withStyle(ChatFormatting.GRAY));
        }
        Tuple<String, List<MutableComponent>> nbtInfo = this.getNbtInfo();
        TooltipKeyManager.PressState nbtState = KEY_MANAGER.nbt();
        if (nbtInfo != null && nbtState.show()) {
            hasInfo = true;
            if (nbtState.showDetail()) {
                first.append(Component.literal("[%s %s] ".formatted(TooltipKeyManager.nbtKeyTranslation(), I18n.get(nbtInfo.getA()))).withStyle(ChatFormatting.WHITE));
                infos = nbtInfo.getB().stream().map(x -> x.withStyle(ChatFormatting.DARK_GRAY)).toList();
            } else
                first.append(Component.literal("[%s %s] ".formatted(TooltipKeyManager.nbtKeyTranslation(), I18n.get(nbtInfo.getA()))).withStyle(ChatFormatting.GRAY));
        }
        return hasInfo ? ImmutableList.<MutableComponent>builder().add(first).addAll(infos).build() : List.of();
    }

    @Nullable
    private Tuple<String, List<MutableComponent>> getItemTagInfo() {
        if (!this.itemTags.isEmpty())
            return new Tuple<>("tooltip.tooltips_reforged.item_tags", this.itemTags.stream().map(Component::literal).toList());
        return null;
    }

    @Nullable
    private Tuple<String, List<MutableComponent>> getSpecificInfo() {
        if (!this.blockTags.isEmpty())
            return new Tuple<>("tooltip.tooltips_reforged.block_tags", this.blockTags.stream().map(Component::literal).toList());
        if (!this.entityTags.isEmpty())
            return new Tuple<>("tooltip.tooltips_reforged.entity_tags", this.entityTags.stream().map(Component::literal).toList());
        if (this.lootTable != null && this.lootTable.left() != null)
            return new Tuple<>("tooltip.tooltips_reforged.loot_table", List.of(
                    Component.literal("tooltip.tooltips_reforged.id" + this.lootTable.left().toString()),
                    Component.literal("tooltip.tooltips_reforged.seed" + this.lootTable.rightLong())
            ));
        return null;
    }

    @Nullable
    private Tuple<String, List<MutableComponent>> getNbtInfo() {
        if (!this.entityInfo.isEmpty())
            return new Tuple<>("tooltip.tooltips_reforged.mob_info", this.entityInfo);
        if (!this.nbt.isEmpty())
            return new Tuple<>("tooltip.tooltips_reforged.nbt", this.nbt);
        return null;
    }
}

//?}
