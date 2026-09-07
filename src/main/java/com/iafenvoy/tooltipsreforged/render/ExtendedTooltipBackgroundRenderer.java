package com.iafenvoy.tooltipsreforged.render;

//? if <1.21 {
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ExtendedTooltipBackgroundRenderer {
    private static final ResourceLocation DEFAULT_BACKGROUND_TEXTURE = ResourceLocation.tryBuild(ResourceLocation.DEFAULT_NAMESPACE, "textures/gui/sprites/tooltip/background.png");
    private static final ResourceLocation DEFAULT_FRAME_TEXTURE = ResourceLocation.tryBuild(ResourceLocation.DEFAULT_NAMESPACE, "textures/gui/sprites/tooltip/frame.png");

    public static void render(ItemStack stack, GuiGraphics context, int x, int y, int width, int height, int z) {
        ResourceLocation texture = stack.getTag() != null ? ResourceLocation.tryParse(stack.getTag().getString("tooltip_style")) : null;
        int i = x - 4;
        int j = y - 4;
        int k = width + 8;
        int l = height + 8;

        context.pose().pushPose();
        //Background
        int bgColor = TooltipReforgedConfig.INSTANCE.misc.backgroundColor.getValue();
        if (TooltipReforgedConfig.INSTANCE.misc.useImageBackground.getValue()) {
            context.pose().translate(0, 0, z);
            TooltipsRenderHelper.drawNineSlicedTexture(context, getBackgroundTexture(texture), i - 9, j - 9, k + 18, l + 18, 10, 100, 100);
        } else {
            context.fill(i, j - 1, i + k, j, z, bgColor);
            context.fill(i, j + l, i + k, j + l + 1, z, bgColor);
            context.fill(i, j, i + k, j + l, z, TooltipReforgedConfig.INSTANCE.misc.backgroundColor.getValue());
            context.fillGradient(i - 1, j, i, j + l, z, bgColor, bgColor);
            context.fillGradient(i + k, j, i + k + 1, j + l, z, bgColor, bgColor);
        }
        //Border
        int stackColor = TooltipProviders.getItemBorderColor(stack);
        int startColor = 0xff000000 | stackColor;
        if (stackColor == -1 || stackColor == 0)
            startColor = TooltipReforgedConfig.INSTANCE.misc.commonRarityColor.getValue();
        int endColor = TooltipReforgedConfig.INSTANCE.misc.endColor.getValue();

        if (TooltipReforgedConfig.INSTANCE.misc.useImageBorder.getValue())
            TooltipsRenderHelper.drawNineSlicedTexture(context, getFrameTexture(texture), i - 10, j - 10, k + 20, l + 20, 10, 100, 100);
        else {
            context.fillGradient(i, j + 1, i + 1, j + l - 1, z, startColor, endColor);
            context.fillGradient(i + k - 1, j + 1, i + k, j + l - 1, z, startColor, endColor);
            context.fill(i, j, i + k, j + 1, z, startColor);
            context.fill(i, j + l - 1, i + k, j + l, z, endColor);
        }
        context.pose().popPose();
    }

    protected static ResourceLocation getBackgroundTexture(@Nullable ResourceLocation texture) {
        return texture == null || texture.getPath().isEmpty() ? DEFAULT_BACKGROUND_TEXTURE : texture.withPath((name) -> "textures/gui/sprites/tooltip/" + name + "_background.png");
    }

    protected static ResourceLocation getFrameTexture(@Nullable ResourceLocation texture) {
        return texture == null || texture.getPath().isEmpty() ? DEFAULT_FRAME_TEXTURE : texture.withPath((name) -> "textures/gui/sprites/tooltip/" + name + "_frame.png");
    }
}

//?} else {
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.Nullable;

public class ExtendedTooltipBackgroundRenderer {
    private static final ResourceLocation DEFAULT_BACKGROUND_TEXTURE = ResourceLocation.fromNamespaceAndPath(ResourceLocation.DEFAULT_NAMESPACE, "textures/gui/sprites/tooltip/background.png");
    private static final ResourceLocation DEFAULT_FRAME_TEXTURE = ResourceLocation.fromNamespaceAndPath(ResourceLocation.DEFAULT_NAMESPACE, "textures/gui/sprites/tooltip/frame.png");

    public static void render(ItemStack stack, GuiGraphics context, int x, int y, int width, int height, int z) {
        CustomData component = stack.get(DataComponents.CUSTOM_DATA);
        ResourceLocation texture = component != null ? ResourceLocation.tryParse(component.copyTag().getString("tooltip_style")) : null;
        int i = x - 4;
        int j = y - 4;
        int k = width + 8;
        int l = height + 8;

        context.pose().pushPose();
        //Background
        int bgColor = TooltipReforgedConfig.INSTANCE.misc.backgroundColor.getValue();
        if (TooltipReforgedConfig.INSTANCE.misc.useImageBackground.getValue()) {
            context.pose().translate(0, 0, z);
            TooltipsRenderHelper.drawNineSlicedTexture(context, getBackgroundTexture(texture), i - 9, j - 9, k + 18, l + 18, 10, 100, 100);
        } else {
            context.fill(i, j - 1, i + k, j, z, bgColor);
            context.fill(i, j + l, i + k, j + l + 1, z, bgColor);
            context.fill(i, j, i + k, j + l, z, TooltipReforgedConfig.INSTANCE.misc.backgroundColor.getValue());
            context.fillGradient(i - 1, j, i, j + l, z, bgColor, bgColor);
            context.fillGradient(i + k, j, i + k + 1, j + l, z, bgColor, bgColor);
        }
        //Border
        int stackColor = TooltipProviders.getItemBorderColor(stack);
        int startColor = 0xff000000 | stackColor;
        if (stackColor == -1 || stackColor == 0)
            startColor = TooltipReforgedConfig.INSTANCE.misc.commonRarityColor.getValue();
        int endColor = TooltipReforgedConfig.INSTANCE.misc.endColor.getValue();

        if (TooltipReforgedConfig.INSTANCE.misc.useImageBorder.getValue())
            TooltipsRenderHelper.drawNineSlicedTexture(context, getFrameTexture(texture), i - 10, j - 10, k + 20, l + 20, 10, 100, 100);
        else {
            context.fillGradient(i, j + 1, i + 1, j + l - 1, z, startColor, endColor);
            context.fillGradient(i + k - 1, j + 1, i + k, j + l - 1, z, startColor, endColor);
            context.fill(i, j, i + k, j + 1, z, startColor);
            context.fill(i, j + l - 1, i + k, j + l, z, endColor);
        }
        context.pose().popPose();
    }

    protected static ResourceLocation getBackgroundTexture(@Nullable ResourceLocation texture) {
        return texture == null || texture.getPath().isEmpty() ? DEFAULT_BACKGROUND_TEXTURE : texture.withPath((name) -> "textures/gui/sprites/tooltip/" + name + "_background.png");
    }

    protected static ResourceLocation getFrameTexture(@Nullable ResourceLocation texture) {
        return texture == null || texture.getPath().isEmpty() ? DEFAULT_FRAME_TEXTURE : texture.withPath((name) -> "textures/gui/sprites/tooltip/" + name + "_frame.png");
    }
}

//?}
