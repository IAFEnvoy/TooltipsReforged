package com.iafenvoy.tooltipsreforged.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.FormattedCharSink;

public class ExtendedTextVisitor {
    public static Component getText(FormattedCharSequence text) {
        TextVisitor visitor = new TextVisitor();
        text.accept(visitor);
        return visitor.getText();
    }

    public static String getString(Component text) {
        StringVisitor visitor = new StringVisitor();
        text.getVisualOrderText().accept(visitor);
        return visitor.getString();
    }

    private static class TextVisitor implements FormattedCharSink {
        private final MutableComponent text = Component.empty();

        @Override
        public boolean accept(int index, Style style, int codePoint) {
            this.text.append(Component.literal(new String(Character.toChars(codePoint))).setStyle(style));
            return true;
        }

        public Component getText() {
            return this.text;
        }
    }

    private static class StringVisitor implements FormattedCharSink {
        private final StringBuilder sb = new StringBuilder();

        @Override
        public boolean accept(int index, Style style, int codePoint) {
            this.sb.append(new String(Character.toChars(codePoint)));
            return true;
        }

        public String getString() {
            return this.sb.toString();
        }
    }
}

