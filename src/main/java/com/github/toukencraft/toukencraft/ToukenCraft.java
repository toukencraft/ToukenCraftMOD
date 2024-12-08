package com.github.toukencraft.toukencraft;

import com.github.toukencraft.toukencraft.init.ToukenCraftEntities;
import com.github.toukencraft.toukencraft.init.ToukenCraftItems;
import com.github.toukencraft.toukencraft.init.ToukenCraftMenus;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/** 刀剣クラフトを登録するクラス */
public class ToukenCraft implements ModInitializer {
    public static final String MOD_ID = "toukencraft";
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing ToukenCraftMod");

        ToukenCraftEntities.load();
        ToukenCraftItems.load();
        ToukenCraftMenus.load();
    }
}
