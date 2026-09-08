package com.iafenvoy.tooltipsreforged.util;

import net.minecraft.resources.ResourceLocation;

public final class RLUtil {
    public static ResourceLocation id(String namespace, String path) {
        //? if <1.21 {
        /*return ResourceLocation.tryBuild(namespace, path);
        *///?} else {
         return ResourceLocation.fromNamespaceAndPath(namespace, path);
        //?}
    }
}
