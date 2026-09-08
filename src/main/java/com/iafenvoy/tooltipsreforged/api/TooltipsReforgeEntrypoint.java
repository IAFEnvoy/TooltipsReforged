package com.iafenvoy.tooltipsreforged.api;

import com.iafenvoy.integration.entrypoint.IntegrationEntryPoint;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
//? if >=1.21 {
import net.minecraft.core.RegistryAccess;
//?}
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface TooltipsReforgeEntrypoint extends IntegrationEntryPoint {
    void appendTooltip(ItemStack stack, List<ClientTooltipComponent> components/*? if >=1.21 {*/, RegistryAccess registries/*?}*/);
}
