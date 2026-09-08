package com.iafenvoy.tooltipsreforged.component;
import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.config.mode.DurabilityRenderMode;
import com.iafenvoy.tooltipsreforged.render.RenderHelper;
import com.iafenvoy.tooltipsreforged.util.BadgesUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import java.util.List;
public class DurabilityComponent implements ClientTooltipComponent, RenderHelper {
    private static final int WIDTH = 80;
    protected final ItemStack stack;
    private final DurabilityRenderMode mode;
    private final boolean enabled;
    private final int color;
    private final Component text;
    public DurabilityComponent(ItemStack stack) {
        this.stack = stack;
        this.mode = TooltipReforgedConfig.INSTANCE.tooltip.durabilityTooltip.getValue();
        this.enabled = this.mode.isEnabled() && this.stack.isDamageableItem() && this.stack.getMaxDamage() > 0;
        this.color = stack.getBarColor();
        this.text = this.enabled ? getDurabilityText(stack, this.mode) : Component.literal("");
    }
    private static Component getDurabilityText(ItemStack stack, DurabilityRenderMode mode) {
        int maxDamage = stack.getMaxDamage(), damaged = maxDamage - stack.getDamageValue();
        if (mode.shouldInPercentage() && maxDamage > 0) {
            Component percentageText = Component.literal(" " + (damaged * 100 / maxDamage) + "%");
            return mode.shouldColorText() ? firstOf(percentageText.toFlatList(Style.EMPTY.withColor(stack.getBarColor()))) : percentageText;
        } else return mode.shouldColorText() ? Component.literal(" ")
                .append(Component.literal(String.valueOf(damaged)).setStyle(Style.EMPTY.withColor(stack.getBarColor())))
                .append(Component.literal(" / ").setStyle(Style.EMPTY.withColor(-8355712)))
                .append(Component.literal(String.valueOf(maxDamage)).setStyle(Style.EMPTY.withColor(0xFF00FF00)))
                : Component.literal(" " + damaged + " / " + maxDamage);
    }
    @Override
    public int getHeight() {
        return this.enabled ? this.mode.shouldRenderBackground() ? 13 : 9 : 0;
    }
    @Override
    public int getWidth(Font textRenderer) {
        if (!this.enabled) return 0;
        int width = textRenderer.width(Component.translatable("tooltip.%s.durability".formatted(TooltipReforgedClient.MOD_ID)));
        if (this.mode.shouldRenderBackground()) return width + SPACING + WIDTH - 5;
        else return width + textRenderer.width(this.text);
    }
    @Override
    public void renderImage(Font textRenderer, int x, int y, GuiGraphics context) {
        if (!this.enabled) return;
        boolean background = this.mode.shouldRenderBackground();
        if (background) y += 2;
        int textHeight = textRenderer.lineHeight;
        int textY = background ? y - textHeight + SPACING * 2 + 2 : y;
        Component durability = Component.translatable("tooltip.%s.durability".formatted(TooltipReforgedClient.MOD_ID));
        context.drawString(textRenderer, durability, x, textY, 0xffffffff, true);
        x += textRenderer.width(durability) + SPACING - 4;
        int damaged = this.stack.getMaxDamage() - this.stack.getDamageValue();
        if (background) {
            int color = BadgesUtils.darkenColor(0xff000000 | this.color, 0.9f);
            context.fill(x, textY - SPACING / 2 + 1, x + (damaged * WIDTH) / this.stack.getMaxDamage(), textY + textHeight - 1, color);
            BadgesUtils.drawFrame(context, x, textY - SPACING / 2 + 1, WIDTH, textHeight + SPACING - 2, 400, BadgesUtils.darkenColor(0xff000000 | this.stack.getBarColor(), 0.8f));
        }
        int textX = background ? x + ((WIDTH - textRenderer.width(this.text)) / 2) - 1 : x - 4;
        context.drawString(textRenderer, this.text, textX, textY, 0xFFFFFFFF, true);
    }
    private static Component firstOf(List<Component> texts) {
        //? if <1.21 {
        /*return texts.get(0);
        *///?} else {
        return texts.getFirst();
        //?}
    }
}
