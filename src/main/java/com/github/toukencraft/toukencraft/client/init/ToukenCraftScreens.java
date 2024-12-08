package com.github.toukencraft.toukencraft.client.init;

import com.github.toukencraft.toukencraft.client.gui.ToukenGuiScreen;
import com.github.toukencraft.toukencraft.init.ToukenCraftMenus;
import net.minecraft.client.gui.screens.MenuScreens;


/** 画面を登録するクラス */
public class ToukenCraftScreens {
    public static void load() {
        MenuScreens.register(ToukenCraftMenus.TOUKEN_INVENTORY, ToukenGuiScreen::new);
    }
}
