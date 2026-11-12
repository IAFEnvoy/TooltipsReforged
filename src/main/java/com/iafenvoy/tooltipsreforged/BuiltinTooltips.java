package com.iafenvoy.tooltipsreforged;

import com.iafenvoy.tooltipsreforged.component.*;
import com.iafenvoy.tooltipsreforged.mixin.DecorationItemAccessor;
import com.iafenvoy.tooltipsreforged.util.TextUtil;
//? if <1.21 {
/*import com.iafenvoy.tooltipsreforged.util.ExtendedTextVisitor;
*///?}
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
//? if >=1.21 {
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
//?}
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.LingeringPotionItem;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.TippedArrowItem;
//? if <1.21 {
/*import net.minecraft.world.item.PlayerHeadItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
*///?} else {
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.SkullBlock;
//?}
import java.util.LinkedList;
import java.util.List;

public class BuiltinTooltips {
    public static void appendTooltip(ItemStack stack, List<ClientTooltipComponent> components/*? if >=1.21 {*/, RegistryAccess registries/*?}*/) {
        FormattedCharSequence text = null;
        if (!components.isEmpty()) {
            ClientTooltipComponent component = components.remove(0);
            if (component instanceof ClientTextTooltip ordered)
                text = TextUtil.getTextFromComponent(ordered);
        }
        List<ClientTooltipComponent> headers = new LinkedList<>();
        //Header
        headers.add(new HeaderComponent(stack, text));
        headers.add(new ItemZoomComponent(stack));
        //Effects
        if (stack.getItem() instanceof LingeringPotionItem)
            headers.add(new PotionEffectsComponent(stack, 0.25f));
        else if (stack.getItem() instanceof PotionItem)
            headers.add(new PotionEffectsComponent(stack, 1));
        else if (stack.getItem() instanceof TippedArrowItem)
            headers.add(new PotionEffectsComponent(stack, 0.125f));
        //Food
        //? if <1.21 {
        /*if (stack.getItem().getFoodProperties() != null)
        *///?} else {
        if (stack.has(DataComponents.FOOD))
        //?}
            headers.add(new FoodEffectComponent(stack));
        //Enchantments
        //? if <1.21 {
        /*if (stack.getItem() instanceof EnchantedBookItem)
            headers.add(new EnchantmentsComponent(EnchantedBookItem.getEnchantments(stack)));
        if (stack.getItem().isEnchantable(stack))
            headers.add(new EnchantmentsComponent(EnchantmentHelper.getEnchantments(stack)));
        *///?} else {
        if (stack.getItem() instanceof EnchantedBookItem)
            headers.add(new EnchantmentsComponent(stack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY)));
        if (stack.getItem().isEnchantable(stack))
            headers.add(new EnchantmentsComponent(stack.getEnchantments()));
        //?}
        components.addAll(0, headers);
        //Equipments
        //? if <1.21 {
        /*if (stack.getItem() instanceof Equipable || stack.getItem() instanceof PlayerHeadItem) {
        *///?} else {
        if (stack.getItem() instanceof Equipable || stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof SkullBlock) {
        //?}
            components.add(new EquipmentViewerComponent(stack));
            components.add(new EquipmentCompareComponent(stack));
        }
        //Misc
        if (stack.getItem() instanceof MobBucketItem || stack.getItem() instanceof SpawnEggItem)
            components.add(new EntityViewerComponent(stack));
        components.add(new ContainerPreviewComponent(stack/*? if >=1.21 {*/, registries/*?}*/));
        if (stack.getItem() instanceof MapItem)
            components.add(new MapComponent(stack));
        if (stack.getItem() instanceof DecorationItemAccessor accessor && accessor.getEntityType() == EntityType.PAINTING)
            components.add(new PaintingComponent(stack));
        if (stack.getItem() instanceof BannerPatternItem || stack.getItem() instanceof BannerItem)
            components.add(new BannerPatternComponent(stack/*? if >=1.21 {*/, registries/*?}*/));
        //Debug & Durability
        //? if <1.21 {
        /*if (Minecraft.getInstance().options.advancedItemTooltips) {
            for (int i = 0; i < components.size(); i++) {
                ClientTooltipComponent component = components.get(i);
                if (component instanceof ClientTextTooltip ordered)
                    if (ExtendedTextVisitor.getText(TextUtil.getTextFromComponent(ordered)).getString().contains((stack.getMaxDamage() - stack.getDamageValue()) + " / " + stack.getMaxDamage())) {
                        components.set(i, new DurabilityComponent(stack));
                        break;
                    }
            }
            components.add(new DebugInfoComponent(stack));
        } else
            components.add(new DurabilityComponent(stack));
        *///?} else {
        components.add(new DurabilityComponent(stack));
        if (Minecraft.getInstance().options.advancedItemTooltips)
            components.add(new DebugInfoComponent(stack, registries));
        //?}
    }
}
