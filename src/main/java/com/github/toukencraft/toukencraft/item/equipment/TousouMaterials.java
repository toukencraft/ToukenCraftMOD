package com.github.toukencraft.toukencraft.item.equipment;

import com.github.toukencraft.toukencraft.ToukenCraft;
import com.google.common.collect.Maps;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.Map;

public class TousouMaterials {
    public static final Holder<ArmorMaterial> TOUSOU_BRONZE_MATERIAL = Holder.direct(Registry.register(
            BuiltInRegistries.ARMOR_MATERIAL,
            ResourceLocation.fromNamespaceAndPath(ToukenCraft.MOD_ID, "tousou_bronze"),
            new ArmorMaterial(
                Maps.newEnumMap(Map.of(ArmorItem.Type.CHESTPLATE, 10)),
                8,
                SoundEvents.ARMOR_EQUIP_IRON,
                () -> Ingredient.of(Items.COPPER_INGOT),
                List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(ToukenCraft.MOD_ID, "tousou_bronze"))),
                0,
                0
            )
    ));
    public static final Holder<ArmorMaterial> TOUSOU_SILVER_MATERIAL = Holder.direct(Registry.register(
            BuiltInRegistries.ARMOR_MATERIAL,
            ResourceLocation.fromNamespaceAndPath(ToukenCraft.MOD_ID, "tousou_silver"),
            new ArmorMaterial(
                Maps.newEnumMap(Map.of(ArmorItem.Type.CHESTPLATE, 10)),
                9,
                SoundEvents.ARMOR_EQUIP_IRON,
                () -> Ingredient.of(Items.IRON_INGOT),
                List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(ToukenCraft.MOD_ID, "tousou_silver"))),
                0,
                0
            )
    ));
    public static final Holder<ArmorMaterial> TOUSOU_GOLD_MATERIAL = Holder.direct(Registry.register(
            BuiltInRegistries.ARMOR_MATERIAL,
            ResourceLocation.fromNamespaceAndPath(ToukenCraft.MOD_ID, "tousou_gold"),
            new ArmorMaterial(
                Maps.newEnumMap(Map.of(ArmorItem.Type.CHESTPLATE, 10)),
                10,
                SoundEvents.ARMOR_EQUIP_IRON,
                () -> Ingredient.of(Items.GOLD_INGOT),
                List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(ToukenCraft.MOD_ID, "tousou_gold"))),
                2,
                0
            )
    ));
}
