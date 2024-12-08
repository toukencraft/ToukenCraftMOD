package com.github.toukencraft.toukencraft.client;

import com.github.toukencraft.toukencraft.client.init.ToukenCraftEntityRenderers;
import com.github.toukencraft.toukencraft.client.init.ToukenCraftScreens;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;


/** クライアント側で刀剣クラフトを登録するクラス */
@Environment(EnvType.CLIENT)
public class ToukenCraftClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ToukenCraftEntityRenderers.load();
        ToukenCraftScreens.load();
    }
}
