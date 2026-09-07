package com.iafenvoy.tooltipsreforged.component;

//? if <1.21 {
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.render.TooltipsRenderHelper;
import com.iafenvoy.tooltipsreforged.util.ExtendedTextVisitor;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class EquipmentCompareComponent extends StandaloneComponent {
    private final List<ClientTooltipComponent> components = new LinkedList<>();
    private final ItemStack equipped;

    public EquipmentCompareComponent(ItemStack stack) {
        super(stack);
        if (!TooltipReforgedConfig.INSTANCE.tooltip.equipmentCompareTooltip.getValue() || !(this.stack.getItem() instanceof Equipable equipment)) {
            this.equipped = ItemStack.EMPTY;
            return;
        }
        LocalPlayer player = Minecraft.getInstance().player;
        assert player != null;
        this.equipped = player.getItemBySlot(equipment.getEquipmentSlot());
        if (this.equipped.isEmpty() || ItemStack.isSameItemSameTags(this.equipped, this.stack)) return;
        for (Component text : this.equipped.getTooltipLines(player, TooltipFlag.NORMAL))
            if (!TooltipReforgedConfig.INSTANCE.misc.removeEmptyLines.getValue() || !ExtendedTextVisitor.getText(text.getVisualOrderText()).getString().isEmpty())
                this.components.add(new ClientTextTooltip(text.getVisualOrderText()));
        if (!this.components.isEmpty()) this.components.remove(0);
        List<ClientTooltipComponent> headers = new LinkedList<>();
        headers.add(new ClientTextTooltip(Component.translatable("tooltip.tooltips_reforged.currently_equipped").getVisualOrderText()));
        headers.add(new HeaderComponent(this.equipped, null));
        if (this.equipped.getItem().isEnchantable(this.equipped))
            headers.add(new EnchantmentsComponent(EnchantmentHelper.getEnchantments(this.equipped)));
        this.components.addAll(0, headers);
        this.components.add(new DurabilityComponent(this.equipped));
    }

    @Override
    public void render(GuiGraphics context, Font textRenderer, int x, int y, int z) {
        if (this.components.isEmpty()) return;
        TooltipsRenderHelper.ResolveResult result = TooltipsRenderHelper.resolveTooltips(textRenderer, this.components);
        if (result.pages().isEmpty()) return;
        TooltipsRenderHelper.Page page = result.pages().get(0);
        int height = page.getHeight();
        TooltipsRenderHelper.drawWithResult(result, this.equipped, context, textRenderer, x, y - height - 20);
    }
}

//?} else {
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.render.TooltipsRenderHelper;
import com.iafenvoy.tooltipsreforged.util.ExtendedTextVisitor;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class EquipmentCompareComponent extends StandaloneComponent {
    private final List<ClientTooltipComponent> components = new LinkedList<>();
    private final ItemStack equipped;

    public EquipmentCompareComponent(ItemStack stack) {
        super(stack);
        if (!TooltipReforgedConfig.INSTANCE.tooltip.equipmentCompareTooltip.getValue() || !(this.stack.getItem() instanceof Equipable equipment)) {
            this.equipped = ItemStack.EMPTY;
            return;
        }
        LocalPlayer player = Minecraft.getInstance().player;
        assert player != null;
        this.equipped = player.getItemBySlot(equipment.getEquipmentSlot());
        if (this.equipped.isEmpty() || ItemStack.isSameItemSameComponents(this.equipped, this.stack)) return;
        for (Component text : this.equipped.getTooltipLines(Item.TooltipContext.EMPTY, player, TooltipFlag.NORMAL))
            if (!TooltipReforgedConfig.INSTANCE.misc.removeEmptyLines.getValue() || !ExtendedTextVisitor.getText(text.getVisualOrderText()).getString().isEmpty())
                this.components.add(new ClientTextTooltip(text.getVisualOrderText()));
        if (!this.components.isEmpty()) this.components.removeFirst();
        List<ClientTooltipComponent> headers = new LinkedList<>();
        headers.add(new ClientTextTooltip(Component.translatable("tooltip.tooltips_reforged.currently_equipped").getVisualOrderText()));
        headers.add(new HeaderComponent(this.equipped, null));
        if (this.equipped.getItem().isEnchantable(this.equipped))
            headers.add(new EnchantmentsComponent(this.equipped.getEnchantments()));
        this.components.addAll(0, headers);
        this.components.add(new DurabilityComponent(this.equipped));
    }

    @Override
    public void render(GuiGraphics context, Font textRenderer, int x, int y, int z) {
        if (this.components.isEmpty()) return;
        TooltipsRenderHelper.ResolveResult result = TooltipsRenderHelper.resolveTooltips(textRenderer, this.components);
        if (result.pages().isEmpty()) return;
        TooltipsRenderHelper.Page page = result.pages().getFirst();
        int height = page.getHeight();
        TooltipsRenderHelper.drawWithResult(result, this.equipped, context, textRenderer, x, y - height - 20);
    }
}

//?}
