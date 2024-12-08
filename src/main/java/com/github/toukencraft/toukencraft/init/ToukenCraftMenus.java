package com.github.toukencraft.toukencraft.init;

import com.github.toukencraft.toukencraft.ToukenCraft;
import com.github.toukencraft.toukencraft.menu.ToukenGuiMenu;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;


/** メニューを登録するクラス */
public class ToukenCraftMenus {
    /** 刀剣男士を右クリックしたときのメニュー */
    public static MenuType<ToukenGuiMenu> TOUKEN_INVENTORY;

    public static void load() {
        TOUKEN_INVENTORY = Registry.register(
                BuiltInRegistries.MENU,
                new ResourceLocation(ToukenCraft.MOD_ID, "touken_inventory_menu"),
                new ExtendedScreenHandlerType<>(ToukenGuiMenu::new)
        );
    }
}
