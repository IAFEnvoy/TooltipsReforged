package com.iafenvoy.tooltipsreforged.render;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.Cod;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public interface RenderHelper {
    int SPACING = 4, SHADOW_LIGHT_COLOR = 15728880;

    default void drawStack(GuiGraphics context, ItemStack stack, int x, int y, float size) {
        if (stack.isEmpty()) return;
        Minecraft client = Minecraft.getInstance();
        BakedModel bakedModel = client.getItemRenderer().getModel(stack, client.level, client.player, 0);
        PoseStack matrices = context.pose();
        matrices.pushPose();
        matrices.translate(x + size / 2, y + size / 2, 150);
        //? if <1.21 {
        /*matrices.mulPoseMatrix((new Matrix4f()).scaling(1.0F, -1.0F, 1.0F));
        *///?} else {
        matrices.mulPose((new Matrix4f()).scaling(1.0F, -1.0F, 1.0F));
        //?}
        matrices.scale(size, size, size);
        boolean bl = !bakedModel.usesBlockLight();
        if (bl) Lighting.setupForFlatItems();
        client.getItemRenderer().render(stack, ItemDisplayContext.GUI, false, matrices, context.bufferSource(), 15728880, OverlayTexture.NO_OVERLAY, bakedModel);
        context.flush();
        if (bl) Lighting.setupFor3DItems();
        matrices.popPose();
    }

    default void drawEntity(GuiGraphics context, int centerX, int centerY, int maxSize, float rotationYaw, LivingEntity entity) {
        float pitch = entity.getXRot(), yaw = entity.getYRot(), bodyYaw = entity.yBodyRot, headYaw = entity.yHeadRot;
        entity.setXRot(0);
        entity.setYRot(rotationYaw);
        entity.yBodyRot = rotationYaw;
        entity.yHeadRot = rotationYaw;

        double boxX = entity.getBoundingBox().getXsize(), boxY = entity.getBoundingBox().getYsize(), scale = (boxX + boxY) / 2;
        Quaternionf correctionRotation = entity instanceof Cod || entity instanceof Salmon ? new Quaternionf().rotateZ((float) Math.toRadians(-90)) : new Quaternionf().rotateX((float) Math.toRadians(180));
        Quaternionf combinedRotation = new Quaternionf().rotateY((float) Math.toRadians(rotationYaw)).mul(correctionRotation);
        if (entity instanceof AbstractSchoolingFish) {
            centerY -= 20;
            scale *= 1.5;
        }
        this.drawEntity(context, centerX - boxX / 2, centerY - boxY / 2, (float) (maxSize / scale), combinedRotation, entity);

        entity.setXRot(pitch);
        entity.setYRot(yaw);
        entity.yBodyRot = bodyYaw;
        entity.yHeadRot = headYaw;
    }

    default void drawEntity(GuiGraphics context, double x, double y, float size, Quaternionf rotation, Entity entity) {
        PoseStack matrices = context.pose();
        matrices.pushPose();
        matrices.translate(x, y, 450);
        matrices.scale(-1, 1, 1);
        //? if <1.21 {
        /*matrices.mulPoseMatrix(new Matrix4f().scaling(size, size, size));
        *///?} else {
        matrices.mulPose(new Matrix4f().scaling(size, size, size));
        //?}
        matrices.mulPose(rotation);

        Lighting.setupForEntityInInventory();

        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        dispatcher.setRenderShadow(false);
        dispatcher.render(entity, 0, 0, 0, 0, 1, context.pose(), context.bufferSource(), SHADOW_LIGHT_COLOR);
        dispatcher.setRenderShadow(true);

        matrices.popPose();
        Lighting.setupFor3DItems();
    }
}

