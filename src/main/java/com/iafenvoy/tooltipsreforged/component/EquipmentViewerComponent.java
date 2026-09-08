package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.render.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
//? if <1.21 {
/*import net.minecraft.world.entity.LivingEntity;
*///?} else {
import net.minecraft.world.item.Equipable;
//?}
import net.minecraft.world.item.ItemStack;

public class EquipmentViewerComponent extends StandaloneComponent implements RenderHelper {
    private static final float ROTATION_INCREMENT = 0.2f;
    private static float CURRENT_ROTATION = 0f;
    private static final int ENTITY_OFFSET = 40;

    public EquipmentViewerComponent(ItemStack stack) {
        super(stack);
    }

    @Override
    public void render(GuiGraphics context, Font textRenderer, int x, int y, int tooltipWidth, int z) {
        CURRENT_ROTATION = (CURRENT_ROTATION + ROTATION_INCREMENT) % 360;
        switch (TooltipReforgedConfig.INSTANCE.tooltip.armorTooltip.getValue()) {
            case PLAYER -> this.renderPlayer(context, x, y, tooltipWidth, z);
            case ARMOR_STAND -> this.renderArmorStand(context, x, y, tooltipWidth, z);
        }
    }

    private void renderPlayer(GuiGraphics context, int x, int y, int tooltipWidth, int z) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        EquipmentSlot slot = getPreferredEquipmentSlot(this.stack);
        ItemStack original = player.getItemBySlot(slot);
        player.setItemSlot(slot, this.stack);
        int frameX = this.getFrameX(context, x, tooltipWidth, 40, -ENTITY_OFFSET - 25);
        this.renderBackground(context, frameX, y, 40, 70, z);
        this.drawEntity(context, frameX + 20, y + 65, 35, -CURRENT_ROTATION, player);
        player.setItemSlot(slot, original);
    }

    private void renderArmorStand(GuiGraphics context, int x, int y, int tooltipWidth, int z) {
        ArmorStand armorStand = new ArmorStand(EntityType.ARMOR_STAND, Minecraft.getInstance().level);
        armorStand.setItemSlot(getPreferredEquipmentSlot(this.stack), this.stack);
        int frameX = this.getFrameX(context, x, tooltipWidth, 40, -ENTITY_OFFSET - 25);
        this.renderBackground(context, frameX, y, 40, 70, z);
        armorStand.tick();
        this.drawEntity(context, frameX + 20, y + 65, 35, -CURRENT_ROTATION, armorStand);
    }

    private static EquipmentSlot getPreferredEquipmentSlot(ItemStack stack) {
        //? if <1.21 {
        /*return LivingEntity.getEquipmentSlotForItem(stack);
        *///?} else {
        Equipable equipment = Equipable.get(stack);
        return equipment != null ? equipment.getEquipmentSlot() : EquipmentSlot.MAINHAND;
        //?}
    }
}
