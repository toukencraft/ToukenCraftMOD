package com.github.toukencraft.toukencraft.init;

import com.github.toukencraft.toukencraft.ToukenCraft;
import com.github.toukencraft.toukencraft.data.ToukenEnum;
import com.github.toukencraft.toukencraft.data.TousouEnum;
import com.github.toukencraft.toukencraft.item.ToukenItem;
import com.github.toukencraft.toukencraft.item.equipment.TousouItem;
import com.github.toukencraft.toukencraft.item.equipment.TousouMaterials;
import com.github.toukencraft.toukencraft.item.UchikoItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;

import java.util.HashMap;


/** 刀剣・アイテムを登録するクラス */
public class ToukenCraftItems {
    /** 打粉 */
    public static Item UCHIKO;

    /** 刀装の一覧 */
    public static HashMap<TousouEnum, Item> TOUSOU = new HashMap<>();

    /** 刀剣の一覧 */
    public static HashMap<ToukenEnum, ToukenItem> TOUKEN = new HashMap<>();


    private static void loadTouken(ToukenEnum toukenEnum) {
        var id = ResourceLocation.fromNamespaceAndPath(ToukenCraft.MOD_ID, toukenEnum.property.itemIdentifier());
        var key = ResourceKey.create(Registries.ITEM, id);
        var property = new Item.Properties().sword(toukenEnum.property.toToolMaterial(), 3, -2.4f).setId(key);
        var item = Registry.register(
                BuiltInRegistries.ITEM,
                id,
                new ToukenItem(property, ToukenCraftEntities.TOUKEN_DANSHI.get(toukenEnum))
        );
        TOUKEN.put(toukenEnum, item);
        // TODO 刀帳タブを作成
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT)
                .register(content -> content.accept(item));
    }

    private static void loadTousou(TousouEnum tousouEnum) {
        var id = ResourceLocation.fromNamespaceAndPath(ToukenCraft.MOD_ID, tousouEnum.identifier);
        var key = ResourceKey.create(Registries.ITEM, id);
        var property = TousouItem.properties(tousouEnum.material).setId(key);
        var item = Registry.register(BuiltInRegistries.ITEM, id, new TousouItem(property));
        TOUSOU.put(tousouEnum, item);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT)
                .register(content -> content.accept(item));
    }


    public static void load() {
        {
            var id = ResourceLocation.fromNamespaceAndPath(ToukenCraft.MOD_ID, "uchiko");
            var key = ResourceKey.create(Registries.ITEM, id);
            var property = new Item.Properties().durability(100).setId(key);
            UCHIKO = Registry.register(
                    BuiltInRegistries.ITEM,
                    id,
                    new UchikoItem(property)
            );
            ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                    .register(content -> content.accept(UCHIKO));
        }

        for (var touken : ToukenEnum.values()) {
            loadTouken(touken);
        }

        for (var tousou : TousouEnum.values()) {
            loadTousou(tousou);
        }
    }
}
