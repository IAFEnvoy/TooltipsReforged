package com.iafenvoy.tooltipsreforged.util;

import com.google.common.collect.Lists;
import com.iafenvoy.tooltipsreforged.mixin.OrderedTextTooltipComponentAccessor;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffectInstance;
import java.util.List;
import java.util.Objects;

public final class TextUtil {
    public static FormattedCharSequence getTextFromComponent(ClientTextTooltip component) {
        return ((OrderedTextTooltipComponentAccessor) component).getText();
    }

    public static List<MutableComponent> splitText(MutableComponent text, int chatWidth, Font fontRenderer) {
        int i = 0;
        MutableComponent ichatcomponent = Component.literal("");
        List<MutableComponent> list = Lists.newArrayList();
        List<MutableComponent> chatComponents = Lists.newArrayList(text);

        for (int j = 0; j < chatComponents.size(); ++j) {
            MutableComponent currentChatComponent = chatComponents.get(j);
            String unformattedText = ExtendedTextVisitor.getString(currentChatComponent);
            boolean addToList = false;

            if (unformattedText.contains("\n")) {
                int k = unformattedText.indexOf(10);
                String s1 = unformattedText.substring(k + 1);
                unformattedText = unformattedText.substring(0, k + 1);
                MutableComponent chatcomponenttext = Component.literal(getColorCode(unformattedText) + s1);
                chatcomponenttext.setStyle(currentChatComponent.getStyle());
                chatComponents.add(j + 1, chatcomponenttext);
                addToList = true;
            }
            String textRemovedLastNewline = unformattedText.endsWith("\n") ? unformattedText.substring(0, unformattedText.length() - 1) : unformattedText;
            int textWidth = fontRenderer.width(textRemovedLastNewline);
            MutableComponent newChatComponent = Component.literal(textRemovedLastNewline);
            newChatComponent.setStyle(currentChatComponent.getStyle());

            if (i + textWidth > chatWidth) {
                String s2 = fontRenderer.plainSubstrByWidth(unformattedText, chatWidth - i, false);
                String s3 = s2.length() < unformattedText.length() ? unformattedText.substring(s2.length()) : null;
                if (s3 != null) {
                    int l = s2.lastIndexOf(" ");
                    if (l >= 0 && fontRenderer.width(unformattedText.substring(0, l)) > 0) {
                        s2 = unformattedText.substring(0, l);
                        s3 = unformattedText.substring(l);
                    } else if (i > 0 && !unformattedText.contains(" ")) {
                        s2 = "";
                        s3 = unformattedText;
                    }
                    MutableComponent chatcomponenttext2 = Component.literal(getColorCode(s2) + s3);
                    chatcomponenttext2.setStyle(currentChatComponent.getStyle());
                    chatComponents.add(j + 1, chatcomponenttext2);
                }
                textWidth = fontRenderer.width(s2);
                newChatComponent = Component.literal(s2);
                newChatComponent.setStyle(currentChatComponent.getStyle());
                addToList = true;
            }
            if (i + textWidth <= chatWidth) {
                i += textWidth;
                ichatcomponent.append(newChatComponent);
            } else
                addToList = true;
            if (addToList) {
                list.add(ichatcomponent);
                i = 0;
                ichatcomponent = Component.literal("");
            }
        }
        list.add(ichatcomponent);
        return list;
    }

    public static String getColorCode(String s) {
        String color = "";
        StringBuilder format = new StringBuilder();
        char last = 0;
        for (char c : s.toCharArray()) {
            if (last == '§') {
                if (c == 'r' || ('0' <= c && c <= 'f')) {
                    color = "§" + c;
                    format = new StringBuilder();
                } else if ('k' <= c && c <= 'o') {
                    format.append("§").append(c);
                }
            }
            last = c;
        }
        return color + format;
    }

    public static int getColorFromTranslation(Component text) {
        return getColorFromTranslation(text.getString());
    }

    public static int getColorFromTranslation(String text) {
        char[] charArray = text.toCharArray();
        if (charArray.length < 2) return 0xFFFFFFFF;
        for (int i = 0; i < charArray.length - 1; i++) {
            if (charArray[i] == '§') {
                ChatFormatting formatting = ChatFormatting.getByCode(charArray[i + 1]);
                int color = formatting == null ? 0xFFFFFFFF : Objects.requireNonNullElse(formatting.getColor(), 0xFFFFFFFF);
                if (color != 0xFFFFFFFF) return color;
            }
        }
        return 0xFFFFFFFF;
    }

    public static Component getDurationText(MobEffectInstance effect, float multiplier) {
        if (effect.isInfiniteDuration() || effect.getDuration() >= 60 * 1200) {
            return Component.translatable("effect.duration.infinite");
        } else {
            int i = Mth.floor((float) effect.getDuration() * multiplier);
            return Component.literal(StringUtil.formatTickDuration(i/*? >=1.21 {*/, 20/*?}*/));
        }
    }
}


