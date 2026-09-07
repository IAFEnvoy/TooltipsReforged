package com.iafenvoy.tooltipsreforged.component;

//? if <1.21 {
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.config.mode.EnchantmentSortMode;
import com.iafenvoy.tooltipsreforged.config.mode.EnchantmentsRenderMode;
import com.iafenvoy.tooltipsreforged.render.RenderHelper;
import com.iafenvoy.tooltipsreforged.util.InfoCollectHelper;
import com.iafenvoy.tooltipsreforged.util.RandomHelper;
import com.iafenvoy.tooltipsreforged.util.TextUtil;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EnchantmentsComponent implements ClientTooltipComponent, RenderHelper {
    private final List<EnchantmentInfo> enchantments = new LinkedList<>();
    private final EnchantmentsRenderMode mode;
    private final boolean extraColor;

    public EnchantmentsComponent(ListTag list) {
        this(EnchantmentHelper.deserializeEnchantments(list));
    }

    public EnchantmentsComponent(Map<Enchantment, Integer> map) {
        this.mode = TooltipReforgedConfig.INSTANCE.tooltip.enchantmentTooltip.getValue();
        for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
            Enchantment enchantment = entry.getKey();
            String descriptionKey = enchantment.getDescriptionId() + ".desc";
            this.enchantments.add(new EnchantmentInfo(enchantment, entry.getValue(), I18n.exists(descriptionKey) ? TextUtil.splitText(Component.literal(I18n.get(descriptionKey)), 300, Minecraft.getInstance().font) : List.of()));
        }
        this.enchantments.sort(TooltipReforgedConfig.INSTANCE.misc.enchantmentSort.getValue().getComparator());
        this.extraColor = TooltipReforgedConfig.INSTANCE.misc.advancedEnchantmentColor.getValue();
    }

    private static boolean isShiftDown() {
        long handle = Minecraft.getInstance().getWindow().getWindow();
        return InputConstants.isKeyDown(handle, InputConstants.KEY_LSHIFT) || InputConstants.isKeyDown(handle, InputConstants.KEY_RSHIFT);
    }

    private boolean shouldDisplayDetail() {
        return this.mode.shouldAlwaysDescription() || isShiftDown();
    }

    @Override
    public int getHeight() {
        boolean includeDetail = this.shouldDisplayDetail();
        return this.mode.shouldRender() ? this.enchantments.stream().reduce(0, (p, c) -> p + c.getHeight(includeDetail), Integer::sum) : 0;
    }

    @Override
    public int getWidth(Font textRenderer) {
        boolean includeDetail = this.shouldDisplayDetail();
        return this.mode.shouldRender() ? this.enchantments.stream().reduce(0, (p, c) -> Math.max(p, c.getWidth(textRenderer, includeDetail)), Math::max) : 0;
    }

    @Override
    public void renderImage(Font textRenderer, int x, int y, GuiGraphics context) {
        if (!this.mode.shouldRender()) return;
        int currentY = y;
        for (EnchantmentInfo info : this.enchantments) {
            int currentX = x;
            Component name = getEnchantmentName(info.enchantment, info.level, this.extraColor);
            context.drawString(textRenderer, name, currentX, currentY, -1, true);
            currentX += textRenderer.width(name) + 2;
            this.drawStack(context, new ItemStack(RandomHelper.pick(InfoCollectHelper.getEnchantmentTarget(info.enchantment.category), Items.AIR)), currentX, currentY, 10);
            currentX += 12;
            if (Minecraft.getInstance().options.advancedItemTooltips)
                context.drawString(textRenderer, info.id.toString(), currentX, currentY, 5592405, true);
            currentY += 10;
            if (this.shouldDisplayDetail())
                for (MutableComponent text : info.descriptions) {
                    context.drawString(textRenderer, text.withStyle(ChatFormatting.DARK_GRAY), x, currentY, -1, true);
                    currentY += 10;
                }
        }
    }

    private static Component getEnchantmentName(Enchantment enchantment, int level, boolean extraColor) {
        MutableComponent mutableText = Component.translatable(enchantment.getDescriptionId());
        if (enchantment.isCurse()) mutableText.withStyle(ChatFormatting.RED);
        else if (extraColor && enchantment.getMaxLevel() < level) mutableText.withStyle(ChatFormatting.LIGHT_PURPLE);
        else if (extraColor) mutableText.withStyle(ChatFormatting.GREEN);
        else mutableText.withStyle(ChatFormatting.GRAY);
        if (level != 1 || enchantment.getMaxLevel() != 1)
            mutableText.append(CommonComponents.SPACE).append(Component.translatable("enchantment.level." + level)).append(Component.literal("/").append(Component.translatable("enchantment.level." + enchantment.getMaxLevel())).withStyle(ChatFormatting.DARK_GRAY));
        return mutableText;
    }

    public record EnchantmentInfo(Enchantment enchantment, int level, ResourceLocation id, List<MutableComponent> descriptions) {
        public EnchantmentInfo(Enchantment enchantment, int level, List<MutableComponent> description) {
            this(enchantment, level, Objects.requireNonNullElse(BuiltInRegistries.ENCHANTMENT.getKey(enchantment), ResourceLocation.tryBuild("", "")), description);
        }

        public int getWidth(Font textRenderer, boolean includeDetail) {
            int width = textRenderer.width(getEnchantmentName(this.enchantment, this.level, false)) + 14 + (Minecraft.getInstance().options.advancedItemTooltips ? textRenderer.width(this.id.toString()) : 0);
            if (includeDetail)
                for (MutableComponent text : this.descriptions) width = Math.max(width, textRenderer.width(text));
            return width;
        }

        public int getHeight(boolean includeDetail) {
            return 10 + (includeDetail ? this.descriptions.size() * 10 : 0);
        }
    }
}

//?} else {
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.config.mode.EnchantmentSortMode;
import com.iafenvoy.tooltipsreforged.config.mode.EnchantmentsRenderMode;
import com.iafenvoy.tooltipsreforged.render.RenderHelper;
import com.iafenvoy.tooltipsreforged.util.RandomHelper;
import com.iafenvoy.tooltipsreforged.util.TextUtil;
import com.mojang.blaze3d.platform.InputConstants;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class EnchantmentsComponent implements ClientTooltipComponent, RenderHelper {
    private final List<EnchantmentInfo> enchantments = new LinkedList<>();
    private final EnchantmentsRenderMode mode;
    private final boolean extraColor;

    public EnchantmentsComponent(ItemEnchantments component) {
        this.mode = (EnchantmentsRenderMode) TooltipReforgedConfig.INSTANCE.tooltip.enchantmentTooltip.getValue();
        for (Object2IntMap.Entry<Holder<Enchantment>> entry : component.entrySet()) {
            Holder<Enchantment> enchantment = entry.getKey();
            String descriptionKey = enchantment.value().description().copy() + ".desc";
            Optional<ResourceKey<Enchantment>> optional = enchantment.unwrapKey();
            if (optional.isEmpty()) continue;
            this.enchantments.add(new EnchantmentInfo(enchantment, entry.getIntValue(), optional.get().location(), I18n.exists(descriptionKey) ? TextUtil.splitText(Component.literal(I18n.get(descriptionKey)), 300, Minecraft.getInstance().font) : List.of()));
        }
        this.enchantments.sort(TooltipReforgedConfig.INSTANCE.misc.enchantmentSort.getValue().getComparator());
        this.extraColor = TooltipReforgedConfig.INSTANCE.misc.advancedEnchantmentColor.getValue();
    }

    private static boolean isShiftDown() {
        long handle = Minecraft.getInstance().getWindow().getWindow();
        return InputConstants.isKeyDown(handle, InputConstants.KEY_LSHIFT) || InputConstants.isKeyDown(handle, InputConstants.KEY_RSHIFT);
    }

    private boolean shouldDisplayDetail() {
        return this.mode.shouldAlwaysDescription() || isShiftDown();
    }

    @Override
    public int getHeight() {
        boolean includeDetail = this.shouldDisplayDetail();
        return this.mode.shouldRender() ? this.enchantments.stream().reduce(0, (p, c) -> p + c.getHeight(includeDetail), Integer::sum) : 0;
    }

    @Override
    public int getWidth(Font textRenderer) {
        boolean includeDetail = this.shouldDisplayDetail();
        return this.mode.shouldRender() ? this.enchantments.stream().reduce(0, (p, c) -> Math.max(p, c.getWidth(textRenderer, includeDetail)), Math::max) : 0;
    }

    @Override
    public void renderImage(Font textRenderer, int x, int y, GuiGraphics context) {
        if (!this.mode.shouldRender()) return;
        int currentY = y;
        for (EnchantmentInfo info : this.enchantments) {
            int currentX = x;
            Component name = getEnchantmentName(info.enchantment, info.level, this.extraColor);
            context.drawString(textRenderer, name, currentX, currentY, -1, true);
            currentX += textRenderer.width(name) + 2;
            this.drawStack(context, new ItemStack(RandomHelper.pick(info.enchantment.value().definition().supportedItems().stream().toList(), BuiltInRegistries.ITEM.wrapAsHolder(Items.AIR))), currentX, currentY, 10);
            currentX += 12;
            if (Minecraft.getInstance().options.advancedItemTooltips)
                context.drawString(textRenderer, info.id.toString(), currentX, currentY, 5592405, true);
            currentY += 10;
            if (this.shouldDisplayDetail())
                for (MutableComponent text : info.descriptions) {
                    context.drawString(textRenderer, text.withStyle(ChatFormatting.DARK_GRAY), x, currentY, -1, true);
                    currentY += 10;
                }
        }
    }

    private static Component getEnchantmentName(Holder<Enchantment> enchantment, int level, boolean extraColor) {
        MutableComponent mutableText = enchantment.value().description().copy();
        if (enchantment.is(EnchantmentTags.CURSE)) mutableText.withStyle(ChatFormatting.RED);
        else if (extraColor && enchantment.value().getMaxLevel() < level)
            mutableText.withStyle(ChatFormatting.LIGHT_PURPLE);
        else if (extraColor) mutableText.withStyle(ChatFormatting.GREEN);
        else mutableText.withStyle(ChatFormatting.GRAY);
        if (level != 1 || enchantment.value().getMaxLevel() != 1)
            mutableText.append(CommonComponents.SPACE).append(Component.translatable("enchantment.level." + level)).append(Component.literal("/").append(Component.translatable("enchantment.level." + enchantment.value().getMaxLevel())).withStyle(ChatFormatting.DARK_GRAY));
        return mutableText;
    }

    public record EnchantmentInfo(Holder<Enchantment> enchantment, int level, ResourceLocation id,
                                  List<MutableComponent> descriptions) {
        public int getWidth(Font textRenderer, boolean includeDetail) {
            int width = textRenderer.width(getEnchantmentName(this.enchantment, this.level, false)) + 14 + (Minecraft.getInstance().options.advancedItemTooltips ? textRenderer.width(this.id.toString()) : 0);
            if (includeDetail)
                for (MutableComponent text : this.descriptions) width = Math.max(width, textRenderer.width(text));
            return width;
        }

        public int getHeight(boolean includeDetail) {
            return 10 + (includeDetail ? this.descriptions.size() * 10 : 0);
        }
    }
}

//?}
