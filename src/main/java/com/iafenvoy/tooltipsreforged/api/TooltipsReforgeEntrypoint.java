package com.iafenvoy.tooltipsreforged.api;

//? if <1.21 {
import com.iafenvoy.integration.entrypoint.IntegrationEntryPoint;
import java.util.List;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;

public interface TooltipsReforgeEntrypoint extends IntegrationEntryPoint {
    void appendTooltip(ItemStack stack, List<ClientTooltipComponent> components);
}

//?} else {
import com.iafenvoy.integration.entrypoint.IntegrationEntryPoint;
import java.util.List;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;

public interface TooltipsReforgeEntrypoint extends IntegrationEntryPoint {
    void appendTooltip(ItemStack stack, List<ClientTooltipComponent> components, RegistryAccess registries);
}

//?}
