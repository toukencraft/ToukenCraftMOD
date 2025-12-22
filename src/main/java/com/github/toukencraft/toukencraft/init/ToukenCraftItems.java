package com.github.toukencraft.toukencraft.init;

import com.github.toukencraft.toukencraft.ToukenCraft;
import com.github.toukencraft.toukencraft.data.ToukenEnum;
import com.github.toukencraft.toukencraft.data.TousouEnum;
import com.github.toukencraft.toukencraft.item.ToukenItem;
import com.github.toukencraft.toukencraft.item.UchikoItem;
import com.github.toukencraft.toukencraft.item.equipment.TousouItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.LinkedHashMap;


/** 刀剣・アイテムを登録するクラス */
public class ToukenCraftItems {
    /** 打粉 */
    public static Item UCHIKO;

    /** 刀装の一覧 */
    public static HashMap<TousouEnum, Item> TOUSOU = new LinkedHashMap<>();

    /** 登録された刀剣の一覧 */
    public static HashMap<ToukenEnum, ToukenItem> TOUKEN = new LinkedHashMap<>();

    private static void load_touken(ToukenEnum toukenEnum) {
        var entityType = ToukenCraftEntities.TOUKEN_DANSHI.get(toukenEnum);
        var item = Registry.register(
                BuiltInRegistries.ITEM,
                new ResourceLocation(ToukenCraft.MOD_ID, toukenEnum.property.itemIdentifier()),
                new ToukenItem(toukenEnum.property.toTier(), entityType)
        );
        TOUKEN.put(toukenEnum, item);

        // TODO 刀帳タブを作成
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register(content -> content.accept(item));
    }

    private static void loadTousou(TousouEnum tousouEnum) {
        var item = Registry.register(
                BuiltInRegistries.ITEM,
                new ResourceLocation(ToukenCraft.MOD_ID, tousouEnum.identifier),
                new TousouItem(tousouEnum.material, tousouEnum.properties)
        );
        TOUSOU.put(tousouEnum, item);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register(content -> content.accept(item));
    }

    public static void load() {
        UCHIKO = Registry.register(
                BuiltInRegistries.ITEM,
                new ResourceLocation(ToukenCraft.MOD_ID,  "uchiko"),
                new UchikoItem()
        );
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(content -> content.accept(UCHIKO));

        for (var touken : ToukenEnum.values()) {
            load_touken(touken);
        }

        for (var tousou : TousouEnum.values()) {
            loadTousou(tousou);
        }
    }
}
