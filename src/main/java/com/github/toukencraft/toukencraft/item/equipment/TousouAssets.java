package com.github.toukencraft.toukencraft.item.equipment;

import com.github.toukencraft.toukencraft.ToukenCraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;


public class TousouAssets {
    public static final ResourceKey<EquipmentAsset> TOUSOU_BRONZE_KEY = createKey("tousou_bronze");
    public static final ResourceKey<EquipmentAsset> TOUSOU_SILVER_KEY = createKey("tousou_silver");
    public static final ResourceKey<EquipmentAsset> TOUSOU_GOLD_KEY = createKey("tousou_gold");

    private static ResourceKey<EquipmentAsset> createKey(String path) {
        return ResourceKey.create(
                EquipmentAssets.ROOT_ID,
                ResourceLocation.fromNamespaceAndPath(ToukenCraft.MOD_ID, path)
        );
    }
}
