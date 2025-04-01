package com.github.toukencraft.toukencraft.init;

import com.github.toukencraft.toukencraft.ToukenCraft;
import com.github.toukencraft.toukencraft.data.ToukenEnum;
import com.github.toukencraft.toukencraft.item.ToukenItem;
import com.github.toukencraft.toukencraft.item.UchikoItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

import java.util.HashMap;


/** 刀剣・アイテムを登録するクラス */
public class ToukenCraftItems {
    /** 打粉 */
    public static Item UCHIKO;

    /** 登録された刀剣の一覧 */
    public static HashMap<ToukenEnum, ToukenItem> TOUKEN = new HashMap<>();

    private static void load_touken(ToukenEnum toukenEnum) {
        var entityType = ToukenCraftEntities.TOUKEN_DANSHI.get(toukenEnum);
        var item = Registry.register(
                BuiltInRegistries.ITEM,
                ResourceLocation.fromNamespaceAndPath(ToukenCraft.MOD_ID, toukenEnum.property.itemIdentifier()),
                new ToukenItem(toukenEnum.property.toTier(), entityType)
        );
        TOUKEN.put(toukenEnum, item);

        // TODO 刀帳タブを作成
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register(content -> content.accept(item));
    }

    public static void load() {
        UCHIKO = Registry.register(
                BuiltInRegistries.ITEM,
                ResourceLocation.fromNamespaceAndPath(ToukenCraft.MOD_ID,  "uchiko"),
                new UchikoItem()
        );
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(content -> content.accept(UCHIKO));

        for (var touken : ToukenEnum.values()) {
            load_touken(touken);
        }
    }
}
