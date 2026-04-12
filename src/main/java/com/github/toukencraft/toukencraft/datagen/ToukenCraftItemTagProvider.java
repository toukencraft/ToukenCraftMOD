package com.github.toukencraft.toukencraft.datagen;

import com.github.toukencraft.toukencraft.ToukenCraft;
import com.github.toukencraft.toukencraft.init.ToukenCraftItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;


/** Itemにタグを追加するクラス */
public class ToukenCraftItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
    /** 刀剣のタグ */
    public static final TagKey<Item> TOUKEN_ITEMS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(ToukenCraft.MOD_ID, "touken")
    );

    /** 打粉のタグ */
    public static final TagKey<Item> UCHIKO_ITEMS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(ToukenCraft.MOD_ID, "uchiko")
    );

    /** 刀装のタグ */
    public static final TagKey<Item> TOUSOU_ITEMS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(ToukenCraft.MOD_ID, "tousou")
    );

    public ToukenCraftItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        valueLookupBuilder(TOUKEN_ITEMS).add(ToukenCraftItems.TOUKEN.values().toArray(new Item[0]));
        valueLookupBuilder(UCHIKO_ITEMS).add(ToukenCraftItems.UCHIKO);
        valueLookupBuilder(TOUSOU_ITEMS).add(ToukenCraftItems.TOUSOU.values().toArray(new Item[0]));
    }
}
