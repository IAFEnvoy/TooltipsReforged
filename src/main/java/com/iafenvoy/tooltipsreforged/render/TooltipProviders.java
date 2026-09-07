package com.iafenvoy.tooltipsreforged.render;

//? if <1.21 {
import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.hook.RarityHook;
import com.iafenvoy.tooltipsreforged.util.TextUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.CommonColors;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class TooltipProviders {
    public static Component getRarityName(ItemStack stack) {
        return Component.translatable("rarity.%s.%s".formatted(TooltipReforgedClient.MOD_ID, stack.getRarity().name().toLowerCase())).setStyle(Style.EMPTY.withColor(CommonColors.GRAY));
    }

    public static Component getDisplayName(ItemStack stack) {
        return RarityHook.applyColor(Component.empty(), stack.getRarity()).append(stack.getHoverName());
    }

    public static int getItemBorderColor(ItemStack stack) {
        Integer color = null;
        if (TooltipReforgedConfig.INSTANCE.misc.useNameColor.getValue())
            color = TextUtil.getColorFromTranslation(getDisplayName(stack));
        if (color == null || color == -1) {
            Rarity rarity = stack.getRarity();
            TextColor textColor = RarityHook.applyColor(Component.empty(), rarity).getStyle().getColor();
            if (TooltipReforgedConfig.INSTANCE.misc.overwriteRarityColor.getValue()) {
                if (rarity == Rarity.COMMON)
                    textColor = TextColor.fromRgb(TooltipReforgedConfig.INSTANCE.misc.commonRarityColor.getValue());
                else if (rarity == Rarity.UNCOMMON)
                    textColor = TextColor.fromRgb(TooltipReforgedConfig.INSTANCE.misc.uncommonRarityColor.getValue());
                else if (rarity == Rarity.RARE)
                    textColor = TextColor.fromRgb(TooltipReforgedConfig.INSTANCE.misc.rareRarityColor.getValue());
                else if (rarity == Rarity.EPIC)
                    textColor = TextColor.fromRgb(TooltipReforgedConfig.INSTANCE.misc.epicRarityColor.getValue());
            }
            if (textColor != null) color = textColor.getValue();
            else color = rarity.color.getColor();
            if (color == null || color == 0xFFFFFF) color = 0xFFFFFFFF;
        }
        return color;
    }
}

//?} else {
import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.hook.RarityHook;
import com.iafenvoy.tooltipsreforged.util.TextUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.CommonColors;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class TooltipProviders {
    public static Component getRarityName(ItemStack stack) {
        return Component.translatable("rarity.%s.%s".formatted(TooltipReforgedClient.MOD_ID, stack.getRarity().name().toLowerCase())).setStyle(Style.EMPTY.withColor(CommonColors.GRAY));
    }

    public static Component getDisplayName(ItemStack stack) {
        return RarityHook.applyColor(Component.empty(), stack.getRarity()).append(stack.getHoverName());
    }

    public static int getItemBorderColor(ItemStack stack) {
        Integer color = null;
        if (TooltipReforgedConfig.INSTANCE.misc.useNameColor.getValue())
            color = TextUtil.getColorFromTranslation(getDisplayName(stack));
        if (color == null || color == -1) {
            Rarity rarity = stack.getRarity();
            TextColor textColor = RarityHook.applyColor(Component.empty(), rarity).getStyle().getColor();
            if (TooltipReforgedConfig.INSTANCE.misc.overwriteRarityColor.getValue()) {
                if (rarity == Rarity.COMMON)
                    textColor = TextColor.fromRgb(TooltipReforgedConfig.INSTANCE.misc.commonRarityColor.getValue());
                else if (rarity == Rarity.UNCOMMON)
                    textColor = TextColor.fromRgb(TooltipReforgedConfig.INSTANCE.misc.uncommonRarityColor.getValue());
                else if (rarity == Rarity.RARE)
                    textColor = TextColor.fromRgb(TooltipReforgedConfig.INSTANCE.misc.rareRarityColor.getValue());
                else if (rarity == Rarity.EPIC)
                    textColor = TextColor.fromRgb(TooltipReforgedConfig.INSTANCE.misc.epicRarityColor.getValue());
            }
            if (textColor != null) color = textColor.getValue();
            else color = rarity.color().getColor();
            if (color == null || color == 0xFFFFFF) color = 0xFFFFFFFF;
        }
        return color;
    }
}

//?}
