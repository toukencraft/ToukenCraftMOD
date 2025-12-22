package com.github.toukencraft.toukencraft;

import com.github.toukencraft.toukencraft.datagen.ToukenCraftEntityTypeTagProvider;
import com.github.toukencraft.toukencraft.datagen.ToukenCraftItemTagProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ToukenCraftDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        var pack = fabricDataGenerator.createPack();
        pack.addProvider(ToukenCraftItemTagProvider::new);
        pack.addProvider(ToukenCraftEntityTypeTagProvider::new);
    }
}
