package com.github.toukencraft.toukencraft.datagen;

import com.github.toukencraft.toukencraft.ToukenCraft;
import com.github.toukencraft.toukencraft.init.ToukenCraftItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;


/** Itemにタグを追加するクラス */
public class ToukenCraftItemTagProvider extends FabricTagProvider.ItemTagProvider {
    /** 刀剣のタグ */
    public static final TagKey<Item> TOUKEN_ITEMS = TagKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(ToukenCraft.MOD_ID, "touken")
    );

    /** 打粉のタグ */
    public static final TagKey<Item> UCHIKO_ITEMS = TagKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(ToukenCraft.MOD_ID, "uchiko")
    );

    /** 刀装のタグ */
    public static final TagKey<Item> TOUSOU_ITEMS = TagKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(ToukenCraft.MOD_ID, "tousou")
    );

    public ToukenCraftItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        getOrCreateTagBuilder(TOUKEN_ITEMS).add(ToukenCraftItems.TOUKEN.values().toArray(new Item[0]));
        getOrCreateTagBuilder(UCHIKO_ITEMS).add(ToukenCraftItems.UCHIKO);
        getOrCreateTagBuilder(TOUSOU_ITEMS).add(ToukenCraftItems.TOUSOU.values().toArray(new Item[0]));
    }
}
