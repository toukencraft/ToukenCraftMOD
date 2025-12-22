package com.github.toukencraft.toukencraft.init;

import com.github.toukencraft.toukencraft.ToukenCraft;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class ToukenCraftDataComponents {
    public static final DataComponentType<List<String>> TOUKEN_TAG = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            ResourceLocation.fromNamespaceAndPath(ToukenCraft.MOD_ID, "touken_tag"),
            DataComponentType.<List<String>>builder().persistent(Codec.STRING.listOf()).build()
    );

    public static void load() {
        // NOTE staticフィールドの読み込みを初期化に間に合わせるために必要
    }
}
