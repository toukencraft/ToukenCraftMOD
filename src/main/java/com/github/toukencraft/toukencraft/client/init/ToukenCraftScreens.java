package com.github.toukencraft.toukencraft.client.init;

import com.github.toukencraft.toukencraft.client.gui.ToukenGuiScreen;
import com.github.toukencraft.toukencraft.init.ToukenCraftMenus;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.MenuScreens;


@Environment(EnvType.CLIENT)
public class ToukenCraftScreens {
    public static void load() {
        MenuScreens.register(ToukenCraftMenus.TOUKEN_INVENTORY, ToukenGuiScreen::new);
    }
}
