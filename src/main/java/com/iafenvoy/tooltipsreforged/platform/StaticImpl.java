package com.iafenvoy.tooltipsreforged.platform;

import java.util.HashMap;
import java.util.Map;

//? forge {
/*import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;
*///?}
//? neoforge {
/*import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.IModInfo;
*///?}
//? fabric {
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
//?}

public final class StaticImpl {
    public static Map<String, String> getAllMods() {
        Map<String, String> mods = new HashMap<>();
        //? fabric {
        for (ModContainer modContainer : FabricLoader.getInstance().getAllMods()) {
            if (modContainer.getMetadata().getId().equals("minecraft")) continue;
            mods.put(modContainer.getMetadata().getId(), modContainer.getMetadata().getName());
        }
        //?} else {
        /*for (IModInfo modContainer : ModList.get().getMods()) {
            if (modContainer.getModId().equals("minecraft")) continue;
            mods.put(modContainer.getNamespace(), modContainer.getDisplayName());
        }
        *///?}
        return mods;
    }
}
