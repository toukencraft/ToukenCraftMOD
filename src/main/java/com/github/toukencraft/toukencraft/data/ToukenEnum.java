package com.github.toukencraft.toukencraft.data;

import com.github.toukencraft.toukencraft.ToukenCraft;
import net.minecraft.resources.ResourceLocation;


/** 刀剣男士の一覧 */
public enum ToukenEnum {
    TEST("test", ToukenType.UCHIGATANA, 1.80f);

    public final ToukenProperty property;

    ToukenEnum(ToukenProperty property) {
        this.property = property;
    }

    ToukenEnum(String identifier, ToukenType type, float height) {
        this(new ToukenProperty(
                identifier,
                identifier,
                new ResourceLocation(ToukenCraft.MOD_ID, String.format("textures/entity/%s.png", identifier)),
                type,
                height
        ));
    }
}
