package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.config.mode.EffectsRenderMode;
import com.iafenvoy.tooltipsreforged.util.EffectsHelper;
import com.iafenvoy.tooltipsreforged.util.RLUtil;
import com.iafenvoy.tooltipsreforged.util.TextUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;

public class FoodEffectComponent implements ClientTooltipComponent {
    //? >=1.21 {
    private static final ResourceLocation FOOD_HALF_TEXTURE = ResourceLocation.withDefaultNamespace("hud/food_half");
    private static final ResourceLocation FOOD_FULL_TEXTURE = ResourceLocation.withDefaultNamespace("hud/food_full");
    //?}
    private static final ResourceLocation SATURATION_ICONS_TEXTURE = RLUtil.id(TooltipReforgedClient.MOD_ID, "textures/icons.png");
    protected final ItemStack stack;
    private final EffectsRenderMode effectsMode;

    public FoodEffectComponent(ItemStack stack) {
        this.stack = stack;
        this.effectsMode = TooltipReforgedConfig.INSTANCE.tooltip.effectsTooltip.getValue();
    }

    public FoodProperties getFoodComponent() {
        return EffectsHelper.getFoodProperties(this.stack);
    }

    public int getHunger() {
        return EffectsHelper.getNutrition(this.getFoodComponent());
    }

    public float getSaturation() {
        FoodProperties foodComponent = this.getFoodComponent();
        if (foodComponent == null) return 0;
        return EffectsHelper.getSaturationIncrement(foodComponent);
    }

    private static int getSaturationBars(float saturationIncrement) {
        return (int) Math.ceil(Math.abs(saturationIncrement) / 2f);
    }

    private static String getSaturationBarsText(float saturationIncrement) {
        int bars = getSaturationBars(saturationIncrement);
        return bars > 10 ? "x" + (saturationIncrement < 0 ? -bars : bars) : null;
    }

    @Override
    public int getHeight() {
        int height = 0;
        FoodProperties foodComponent = this.getFoodComponent();
        if (foodComponent != null) {
            if (TooltipReforgedConfig.INSTANCE.tooltip.hungerTooltip.getValue() && EffectsHelper.getNutrition(foodComponent) > 0)
                height += 10;
            if (TooltipReforgedConfig.INSTANCE.tooltip.saturationTooltip.getValue() && this.getSaturation() > 0)
                height += 10;
            if (this.effectsMode.shouldRender())
                height += EffectsHelper.getFoodEffects(foodComponent).size() * 10;
        }
        return height;
    }

    @Override
    public int getWidth(Font textRenderer) {
        int foodWidth, effectsWidth = 0, hungerLine = 0, saturationLine = 0;
        FoodProperties foodComponent = this.getFoodComponent();
        int hunger = this.getHunger();

        if (TooltipReforgedConfig.INSTANCE.tooltip.hungerTooltip.getValue() && EffectsHelper.getNutrition(foodComponent) > 0) {
            int hungerIcons = (hunger + 1) / 2;
            hungerLine = (hungerIcons - 1) * (textRenderer.lineHeight - 2) + textRenderer.lineHeight;
        }
        if (TooltipReforgedConfig.INSTANCE.tooltip.saturationTooltip.getValue() && EffectsHelper.getSaturationIncrement(foodComponent) > 0) {
            float saturation = EffectsHelper.getSaturationIncrement(foodComponent);
            String barsText = getSaturationBarsText(saturation);
            int iconCount = barsText != null ? 1 : getSaturationBars(saturation);
            int saturationWidth = iconCount * 7;
            if (barsText != null)
                saturationWidth += textRenderer.width(barsText);
            saturationLine = saturationWidth;
        }
        foodWidth = Math.max(hungerLine, saturationLine);

        if (!this.effectsMode.shouldRender()) return foodWidth > 0 ? foodWidth + 4 : 0;
        if (foodComponent == null) return 0;
        for (MobEffectInstance statusEffect : EffectsHelper.getFoodEffects(foodComponent)) {
            String text = Component.translatable(statusEffect.getDescriptionId()).getString();
            if (statusEffect.getAmplifier() > 0)
                text = Component.translatable("potion.withAmplifier", text, Component.translatable("potion.potency." + statusEffect.getAmplifier()).getString()).getString();
            if (!statusEffect.endsWithin(20))
                text += " " + TextUtil.getDurationText(statusEffect, 1.0f).getString();
            int extraWidth = this.effectsMode.shouldRenderIcon() ? 14 : 4;
            effectsWidth = Math.max(effectsWidth, textRenderer.width(text) + extraWidth);
        }
        int visibleWidth = Math.max(foodWidth, effectsWidth);
        return visibleWidth > 0 ? visibleWidth + 4 : 0;
    }

    @Override
    public void renderImage(Font textRenderer, int x, int y, GuiGraphics context) {
        FoodProperties foodComponent = this.getFoodComponent();
        if (foodComponent == null) return;
        int hunger = this.getHunger();
        int lineY = y;

        if (TooltipReforgedConfig.INSTANCE.tooltip.hungerTooltip.getValue() && EffectsHelper.getNutrition(foodComponent) > 0) {
            float fullHungers = hunger / 2f;
            boolean hasHalfHunger = (hunger % 2) != 0;
            int hungerWidth = 0;
            //? if <1.21 {
            /*for (int i = 0; i < (int) fullHungers; i++) {
                context.blit(new ResourceLocation("textures/gui/icons.png"), x + hungerWidth - 1, lineY, 52, 27, textRenderer.lineHeight, textRenderer.lineHeight, 256, 256);
                hungerWidth += textRenderer.lineHeight - 2;
            }
            if (hasHalfHunger)
                context.blit(new ResourceLocation("textures/gui/icons.png"), x + hungerWidth - 1, lineY, 61, 27, textRenderer.lineHeight, textRenderer.lineHeight, 256, 256);
            *///?} else {
            for (int i = 0; i < (int) fullHungers; i++) {
                context.blitSprite(FOOD_FULL_TEXTURE, x + hungerWidth - 1, lineY, 9, 9);
                hungerWidth += textRenderer.lineHeight - 2;
            }
            if (hasHalfHunger)
                context.blitSprite(FOOD_HALF_TEXTURE, x + hungerWidth - 1, lineY, 9, 9);
            //?}
            lineY += textRenderer.lineHeight + 1;
        }

        if (TooltipReforgedConfig.INSTANCE.tooltip.saturationTooltip.getValue() && EffectsHelper.getSaturationIncrement(foodComponent) > 0) {
            float saturationIncrement = EffectsHelper.getSaturationIncrement(foodComponent);
            float absSaturationIncrement = Math.abs(saturationIncrement);
            int bars = getSaturationBars(saturationIncrement);
            String barsText = getSaturationBarsText(saturationIncrement);
            if (barsText != null)
                bars = 1;

            // Draw from left to right so a partially filled bar ends up on the right.
            int iconX = x;
            for (int i = 0; i < bars * 2; i += 2) {
                float effectiveSaturationOfBar = (absSaturationIncrement - i) / 2f;
                int u = effectiveSaturationOfBar >= 1f ? 21 : effectiveSaturationOfBar > 0.5f ? 14 : effectiveSaturationOfBar > 0.25f ? 7 : effectiveSaturationOfBar > 0f ? 0 : 28;
                context.blit(SATURATION_ICONS_TEXTURE, iconX, lineY, u, saturationIncrement >= 0f ? 27 : 34, 7, 7, 256, 256);
                iconX += 7;
            }

            if (barsText != null) {
                PoseStack pose = context.pose();
                pose.pushPose();
                pose.translate(iconX, lineY, 0.0F);
                pose.scale(0.75f, 0.75f, 0.75f);
                context.drawString(textRenderer, barsText, 2, 1, 0xFFAAAAAA, true);
                pose.popPose();
            }
            lineY += textRenderer.lineHeight + 1;
        }

        if (!this.effectsMode.shouldRender()) return;
        for (MobEffectInstance statusEffect : EffectsHelper.getFoodEffects(foodComponent)) {
            int c = EffectsHelper.getEffectColor(statusEffect);
            TextureAtlasSprite effectTexture = Minecraft.getInstance().getMobEffectTextures().get(statusEffect.getEffect());
            if (c == 0) c = 0xFF5454FC;

            String effectName = Component.translatable(statusEffect.getDescriptionId()).getString();
            if (statusEffect.getAmplifier() > 0)
                effectName = Component.translatable("potion.withAmplifier", effectName, Component.translatable("potion.potency." + statusEffect.getAmplifier()).getString()).getString();
            String fullText = effectName;
            if (!statusEffect.endsWithin(20))
                fullText += " (" + TextUtil.getDurationText(statusEffect, 1.0f).getString() + ")";
            Component renderText = Component.literal(fullText);
            if (this.effectsMode.shouldRenderIcon()) {
                context.blit(x - 1, lineY - 1, 0, textRenderer.lineHeight, textRenderer.lineHeight, effectTexture);
                context.drawString(textRenderer, renderText, x + textRenderer.lineHeight + 2, lineY, c, true);
            } else context.drawString(textRenderer, renderText, x, lineY, c, true);
            lineY += textRenderer.lineHeight + 1;
        }
    }
}
