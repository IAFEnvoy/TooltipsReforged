package com.iafenvoy.tooltipsreforged;

//? if <1.21 {
import com.iafenvoy.tooltipsreforged.component.*;
import com.iafenvoy.tooltipsreforged.mixin.DecorationItemAccessor;
import com.iafenvoy.tooltipsreforged.util.ExtendedTextVisitor;
import com.iafenvoy.tooltipsreforged.util.TextUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.LingeringPotionItem;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.PlayerHeadItem;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.TippedArrowItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import java.util.LinkedList;
import java.util.List;

public class BuiltinTooltips {
    public static void appendTooltip(ItemStack stack, List<ClientTooltipComponent> components) {
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
        if (stack.getItem().getFoodProperties() != null)
            headers.add(new FoodEffectComponent(stack));
        //Enchantments
        if (stack.getItem() instanceof EnchantedBookItem)
            headers.add(new EnchantmentsComponent(EnchantedBookItem.getEnchantments(stack)));
        if (stack.getItem().isEnchantable(stack))
            headers.add(new EnchantmentsComponent(EnchantmentHelper.getEnchantments(stack)));
        components.addAll(0, headers);
        //Equipments
        if (stack.getItem() instanceof Equipable || stack.getItem() instanceof PlayerHeadItem) {
            components.add(new EquipmentViewerComponent(stack));
            components.add(new EquipmentCompareComponent(stack));
        }
        //Misc
        if (stack.getItem() instanceof MobBucketItem || stack.getItem() instanceof SpawnEggItem)
            components.add(new EntityViewerComponent(stack));
        components.add(new ContainerPreviewComponent(stack));
        if (stack.getItem() instanceof MapItem)
            components.add(new MapComponent(stack));
        if (stack.getItem() instanceof DecorationItemAccessor accessor && accessor.getEntityType() == EntityType.PAINTING)
            components.add(new PaintingComponent(stack));
        if (stack.getItem() instanceof BannerPatternItem)
            components.add(new BannerPatternComponent(stack));
        //Debug & Durability
        if (Minecraft.getInstance().options.advancedItemTooltips) {
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
    }
}

//?} else {
import com.iafenvoy.tooltipsreforged.component.*;
import com.iafenvoy.tooltipsreforged.mixin.DecorationItemAccessor;
import com.iafenvoy.tooltipsreforged.util.TextUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.LingeringPotionItem;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.TippedArrowItem;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.SkullBlock;
import java.util.LinkedList;
import java.util.List;

public class BuiltinTooltips {
    public static void appendTooltip(ItemStack stack, List<ClientTooltipComponent> components, RegistryAccess registries) {
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
        if (stack.has(DataComponents.FOOD))
            headers.add(new FoodEffectComponent(stack));
        //Enchantments
        if (stack.getItem() instanceof EnchantedBookItem)
            headers.add(new EnchantmentsComponent(stack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY)));
        if (stack.getItem().isEnchantable(stack))
            headers.add(new EnchantmentsComponent(stack.getEnchantments()));
        components.addAll(0, headers);
        //Equipments
        if (stack.getItem() instanceof Equipable || stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof SkullBlock) {
            components.add(new EquipmentViewerComponent(stack));
            components.add(new EquipmentCompareComponent(stack));
        }
        //Misc
        if (stack.getItem() instanceof MobBucketItem || stack.getItem() instanceof SpawnEggItem)
            components.add(new EntityViewerComponent(stack));
        components.add(new ContainerPreviewComponent(stack, registries));
        if (stack.getItem() instanceof MapItem)
            components.add(new MapComponent(stack));
        if (stack.getItem() instanceof DecorationItemAccessor accessor && accessor.getEntityType() == EntityType.PAINTING)
            components.add(new PaintingComponent(stack));
        if (stack.getItem() instanceof BannerPatternItem)
            components.add(new BannerPatternComponent(stack, registries));
        //Debug & Durability
        components.add(new DurabilityComponent(stack));
        if (Minecraft.getInstance().options.advancedItemTooltips)
            components.add(new DebugInfoComponent(stack, registries));
    }
}

//?}
