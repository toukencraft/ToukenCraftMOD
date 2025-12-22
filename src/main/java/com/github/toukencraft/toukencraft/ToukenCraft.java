package com.github.toukencraft.toukencraft;

import com.github.toukencraft.toukencraft.init.ToukenCraftDataComponents;
import com.github.toukencraft.toukencraft.init.ToukenCraftEntities;
import com.github.toukencraft.toukencraft.init.ToukenCraftItems;
import com.github.toukencraft.toukencraft.init.ToukenCraftMenus;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/** 刀剣クラフトを登録するクラス */
public class ToukenCraft implements ModInitializer {
	public static final String MOD_ID = "toukencraft";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing ToukenCraftMod");

        ToukenCraftDataComponents.load();
		ToukenCraftEntities.load();
		ToukenCraftItems.load();
		ToukenCraftMenus.load();
	}
}