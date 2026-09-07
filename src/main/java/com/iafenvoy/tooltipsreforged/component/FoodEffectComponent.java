package com.iafenvoy.tooltipsreforged.component;

//? if <1.21 {
import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.config.mode.EffectsRenderMode;
import com.iafenvoy.tooltipsreforged.util.TextUtil;
import com.mojang.datafixers.util.Pair;
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
    protected final ItemStack stack;
    private final EffectsRenderMode effectsMode;

    public FoodEffectComponent(ItemStack stack) {
        this.stack = stack;
        this.effectsMode = TooltipReforgedConfig.INSTANCE.tooltip.effectsTooltip.getValue();
    }

    public FoodProperties getFoodComponent() {
        return this.stack.getItem().getFoodProperties();
    }

    public int getHunger() {
        return this.getFoodComponent().getNutrition();
    }

    public int getSaturation() {
        FoodProperties foodComponent = this.getFoodComponent();
        int saturation = 0;
        if (foodComponent != null)
            saturation = (int) (foodComponent.getSaturationModifier() * 100);
        return saturation;
    }

    @Override
    public int getHeight() {
        int height = 0;
        FoodProperties foodComponent = this.getFoodComponent();
        if (foodComponent != null) {
            if (TooltipReforgedConfig.INSTANCE.tooltip.hungerTooltip.getValue() && foodComponent.getNutrition() > 0)
                height += 10;
            if (TooltipReforgedConfig.INSTANCE.tooltip.saturationTooltip.getValue() && foodComponent.getSaturationModifier() > 0)
                height += 10;
            if (this.effectsMode.shouldRender())
                height += foodComponent.getEffects().size() * 10;
        }
        return height;
    }

    @Override
    public int getWidth(Font textRenderer) {
        int foodWidth, effectsWidth = 0, hungerLine = 0, saturationLine = 0;
        FoodProperties foodComponent = this.getFoodComponent();
        int hunger = this.getHunger();

        if (TooltipReforgedConfig.INSTANCE.tooltip.hungerTooltip.getValue() && foodComponent.getNutrition() > 0)
            hungerLine = textRenderer.width(Component.translatable("tooltip.%s.hunger".formatted(TooltipReforgedClient.MOD_ID))) + 1 + ((textRenderer.lineHeight - 2) * hunger);
        if (TooltipReforgedConfig.INSTANCE.tooltip.saturationTooltip.getValue() && foodComponent.getSaturationModifier() > 0)
            saturationLine = textRenderer.width(Component.translatable("tooltip.%s.saturation".formatted(TooltipReforgedClient.MOD_ID), "100%"));
        foodWidth = Math.max(hungerLine, saturationLine);

        if (!this.effectsMode.shouldRender()) return foodWidth + 4;
        if (foodComponent == null) return 0;
        for (Pair<MobEffectInstance, Float> effect : foodComponent.getEffects()) {
            MobEffectInstance statusEffect = effect.getFirst();
            String text = Component.translatable(statusEffect.getDescriptionId()).getString();
            if (statusEffect.getAmplifier() > 0) {
                text = Component.translatable("potion.withAmplifier", text, Component.translatable("potion.potency." + statusEffect.getAmplifier()).getString()).getString();
            }
            if (!statusEffect.endsWithin(20)) {
                text += " " + TextUtil.getDurationText(statusEffect, 1.0f).getString();
            }
            effectsWidth = Math.max(effectsWidth, textRenderer.width(text) + 14);
        }
        return Math.max(foodWidth, effectsWidth) + 4;
    }

    @Override
    public void renderImage(Font textRenderer, int x, int y, GuiGraphics context) {
        FoodProperties foodComponent = this.getFoodComponent();
        if (foodComponent == null) return;
        int hunger = this.getHunger();
        Component hungerText = Component.translatable("tooltip.%s.hunger".formatted(TooltipReforgedClient.MOD_ID));
        Component saturationText = Component.translatable("tooltip.%s.saturation".formatted(TooltipReforgedClient.MOD_ID), this.getSaturation());
        int lineY = y;

        if (TooltipReforgedConfig.INSTANCE.tooltip.hungerTooltip.getValue() && foodComponent.getNutrition() > 0) {
            context.drawString(textRenderer, hungerText, x, lineY, 0xffffffff, true);
            float fullHungers = hunger / 2f;
            boolean hasHalfHunger = (hunger % 2) != 0;
            int hungerWidth = textRenderer.width(hungerText) + 1;
            for (int i = 0; i < (int) fullHungers; i++) {
                context.blit(new ResourceLocation("textures/gui/icons.png"), x + hungerWidth, lineY, 52, 27, textRenderer.lineHeight, textRenderer.lineHeight, 256, 256);
                hungerWidth += textRenderer.lineHeight - 2;
            }
            if (hasHalfHunger)
                context.blit(new ResourceLocation("textures/gui/icons.png"), x + hungerWidth, lineY, 61, 27, textRenderer.lineHeight, textRenderer.lineHeight, 256, 256);
            lineY += textRenderer.lineHeight + 1;
        }

        if (TooltipReforgedConfig.INSTANCE.tooltip.saturationTooltip.getValue() && foodComponent.getSaturationModifier() > 0) {
            context.drawString(textRenderer, saturationText, x, lineY, 0xff00ffff, true);
            lineY += textRenderer.lineHeight + 1;
        }

        if (!this.effectsMode.shouldRender()) return;
        for (Pair<MobEffectInstance, Float> effect : foodComponent.getEffects()) {
            MobEffectInstance statusEffect = effect.getFirst();
            int c = statusEffect.getEffect().getColor();
            TextureAtlasSprite effectTexture = Minecraft.getInstance().getMobEffectTextures().get(statusEffect.getEffect());
            if (c == 0) c = 0xFF5454FC;

            String effectName = Component.translatable(statusEffect.getDescriptionId()).getString();
            if (statusEffect.getAmplifier() > 0) {
                effectName = Component.translatable("potion.withAmplifier", effectName, Component.translatable("potion.potency." + statusEffect.getAmplifier()).getString()).getString();
            }
            String fullText = effectName;
            if (!statusEffect.endsWithin(20)) {
                fullText += " (" + TextUtil.getDurationText(statusEffect, 1.0f).getString() + ")";
            }
            Component renderText = Component.literal(fullText);
            if (this.effectsMode.shouldRenderIcon()) {
                context.blit(x - 1, lineY - 1, 0, textRenderer.lineHeight, textRenderer.lineHeight, effectTexture);
                context.drawString(textRenderer, renderText, x + textRenderer.lineHeight + 2, lineY, c, true);
            } else context.drawString(textRenderer, renderText, x, lineY, c, true);
            lineY += textRenderer.lineHeight + 1;
        }
    }

}

//?} else {
import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.config.mode.EffectsRenderMode;
import com.iafenvoy.tooltipsreforged.util.TextUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.ItemStack;

public class FoodEffectComponent implements ClientTooltipComponent {
    private static final ResourceLocation FOOD_HALF_TEXTURE = ResourceLocation.withDefaultNamespace("hud/food_half");
    private static final ResourceLocation FOOD_FULL_TEXTURE = ResourceLocation.withDefaultNamespace("hud/food_full");
    protected final ItemStack stack;
    private final EffectsRenderMode effectsMode;

    public FoodEffectComponent(ItemStack stack) {
        this.stack = stack;
        this.effectsMode = TooltipReforgedConfig.INSTANCE.tooltip.effectsTooltip.getValue();
    }

    public FoodProperties getFoodComponent() {
        return this.stack.getOrDefault(DataComponents.FOOD, Foods.APPLE);
    }

    public int getHunger() {
        return this.getFoodComponent().nutrition();
    }

    public int getSaturation() {
        FoodProperties foodComponent = this.getFoodComponent();
        int saturation = 0;
        if (foodComponent != null)
            saturation = (int) (foodComponent.saturation() * 100);
        return saturation;
    }

    @Override
    public int getHeight() {
        int height = 0;
        FoodProperties foodComponent = this.getFoodComponent();
        if (foodComponent != null) {
            if (TooltipReforgedConfig.INSTANCE.tooltip.hungerTooltip.getValue() && foodComponent.nutrition() > 0)
                height += 10;
            if (TooltipReforgedConfig.INSTANCE.tooltip.saturationTooltip.getValue() && foodComponent.saturation() > 0)
                height += 10;
            if (this.effectsMode.shouldRender())
                height += foodComponent.effects().size() * 10;
        }
        return height;
    }

    @Override
    public int getWidth(Font textRenderer) {
        int foodWidth, effectsWidth = 0, hungerLine = 0, saturationLine = 0;
        FoodProperties foodComponent = this.getFoodComponent();
        int hunger = this.getHunger();

        if (TooltipReforgedConfig.INSTANCE.tooltip.hungerTooltip.getValue() && foodComponent.nutrition() > 0)
            hungerLine = textRenderer.width(Component.translatable("tooltip.%s.hunger".formatted(TooltipReforgedClient.MOD_ID))) + 1 + ((textRenderer.lineHeight - 2) * hunger);
        if (TooltipReforgedConfig.INSTANCE.tooltip.saturationTooltip.getValue() && foodComponent.saturation() > 0)
            saturationLine = textRenderer.width(Component.translatable("tooltip.%s.saturation".formatted(TooltipReforgedClient.MOD_ID), "100%"));
        foodWidth = Math.max(hungerLine, saturationLine);

        if (!this.effectsMode.shouldRender()) return foodWidth + 4;
        if (foodComponent == null) return 0;
        for (FoodProperties.PossibleEffect effect : foodComponent.effects()) {
            MobEffectInstance statusEffect = effect.effect();
            String text = Component.translatable(statusEffect.getDescriptionId()).getString();
            if (statusEffect.getAmplifier() > 0)
                text = Component.translatable("potion.withAmplifier", text, Component.translatable("potion.potency." + statusEffect.getAmplifier()).getString()).getString();
            if (!statusEffect.endsWithin(20))
                text += " " + TextUtil.getDurationText(statusEffect, 1.0f).getString();
            effectsWidth = Math.max(effectsWidth, textRenderer.width(text) + 14);
        }
        return Math.max(foodWidth, effectsWidth) + 4;
    }

    @Override
    public void renderImage(Font textRenderer, int x, int y, GuiGraphics context) {
        FoodProperties foodComponent = this.getFoodComponent();
        if (foodComponent == null) return;
        int hunger = this.getHunger();
        Component hungerText = Component.translatable("tooltip.%s.hunger".formatted(TooltipReforgedClient.MOD_ID));
        Component saturationText = Component.translatable("tooltip.%s.saturation".formatted(TooltipReforgedClient.MOD_ID), this.getSaturation());
        int lineY = y;

        if (TooltipReforgedConfig.INSTANCE.tooltip.hungerTooltip.getValue() && foodComponent.nutrition() > 0) {
            context.drawString(textRenderer, hungerText, x, lineY, 0xffffffff, true);
            float fullHungers = hunger / 2f;
            boolean hasHalfHunger = (hunger % 2) != 0;
            int hungerWidth = textRenderer.width(hungerText) + 1;
            for (int i = 0; i < (int) fullHungers; i++) {
                context.blitSprite(FOOD_FULL_TEXTURE, x + hungerWidth, lineY, 9, 9);
                hungerWidth += textRenderer.lineHeight - 2;
            }
            if (hasHalfHunger)
                context.blitSprite(FOOD_HALF_TEXTURE, x + hungerWidth, lineY, 9, 9);
            lineY += textRenderer.lineHeight + 1;
        }

        if (TooltipReforgedConfig.INSTANCE.tooltip.saturationTooltip.getValue() && foodComponent.saturation() > 0) {
            context.drawString(textRenderer, saturationText, x, lineY, 0xff00ffff, true);
            lineY += textRenderer.lineHeight + 1;
        }

        if (!this.effectsMode.shouldRender()) return;
        for (FoodProperties.PossibleEffect effect : foodComponent.effects()) {
            MobEffectInstance statusEffect = effect.effect();
            int c = statusEffect.getEffect().value().getColor();
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

//?}
