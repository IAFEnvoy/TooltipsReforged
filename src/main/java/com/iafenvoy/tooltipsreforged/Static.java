package com.iafenvoy.tooltipsreforged;

import com.iafenvoy.tooltipsreforged.platform.StaticImpl;
import java.util.Map;
import net.minecraft.world.item.ItemStack;

public final class Static {
    public static final ThreadLocal<ItemStack> CACHE = new ThreadLocal<>();

    public static Map<String, String> getAllMods() {
        return StaticImpl.getAllMods();
    }
}
