package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.config.mode.EffectsRenderMode;
import com.iafenvoy.tooltipsreforged.util.EffectsHelper;
import com.iafenvoy.tooltipsreforged.util.TextUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class PotionEffectsComponent implements ClientTooltipComponent {
    private final float durationMultiplier;
    private final List<MobEffectInstance> effects;
    private final EffectsRenderMode effectsMode;

    public PotionEffectsComponent(ItemStack stack, float durationMultiplier) {
        this.durationMultiplier = durationMultiplier;
        this.effects = EffectsHelper.getPotionEffects(stack);
        this.effectsMode = TooltipReforgedConfig.INSTANCE.tooltip.effectsTooltip.getValue();
    }

    @Override
    public int getHeight() {
        return this.effectsMode.shouldRender() ? this.effects.size() * 10 : 0;
    }

    @Override
    public int getWidth(Font textRenderer) {
        if (!this.effectsMode.shouldRender()) return 0;
        int effectsWidth = 0;
        for (MobEffectInstance effect : this.effects) {
            String text = Component.translatable(effect.getDescriptionId()).getString();
            if (effect.getAmplifier() > 0)
                text = Component.translatable("potion.withAmplifier", text, Component.translatable("potion.potency." + effect.getAmplifier()).getString()).getString();
            if (!effect.endsWithin(20))
                text += " " + TextUtil.getDurationText(effect, this.durationMultiplier).getString();
            int extraWidth = this.effectsMode.shouldRenderIcon() ? 14 : 4;
            effectsWidth = Math.max(effectsWidth, textRenderer.width(text) + extraWidth);
        }
        return effectsWidth + 4;
    }

    @Override
    public void renderImage(Font textRenderer, int x, int y, GuiGraphics context) {
        if (!this.effectsMode.shouldRender()) return;
        int lineY = y - textRenderer.lineHeight - 1;
        for (MobEffectInstance effect : this.effects) {
            int c = EffectsHelper.getEffectColor(effect);
            if (c == 0) c = 0xFF5454FC;
            TextureAtlasSprite effectTexture = Minecraft.getInstance().getMobEffectTextures().get(effect.getEffect());
            Component mutableText = Component.translatable(effect.getDescriptionId());
            if (effect.getAmplifier() > 0)
                mutableText = Component.translatable("potion.withAmplifier", mutableText, Component.translatable("potion.potency." + effect.getAmplifier()));
            if (!effect.endsWithin(20)) {
                String durationText = TextUtil.getDurationText(effect, this.durationMultiplier).getString();
                mutableText = Component.literal(mutableText.getString() + " (" + durationText + ")");
            }
            lineY += textRenderer.lineHeight + 1;
            if (this.effectsMode.shouldRenderIcon()) {
                context.blit(x - 1, lineY - 1, 0, textRenderer.lineHeight, textRenderer.lineHeight, effectTexture);
                context.drawString(textRenderer, mutableText, x + textRenderer.lineHeight + 2, lineY, c, true);
            } else context.drawString(textRenderer, mutableText, x, lineY, c, true);
        }
    }
}
