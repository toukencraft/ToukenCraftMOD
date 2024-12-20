package com.github.toukencraft.toukencraft.data;

import com.github.toukencraft.toukencraft.ToukenCraft;
import net.minecraft.resources.ResourceLocation;


/** 刀剣男士の一覧 */
public enum ToukenEnum {
    KASHUU_KIYOMITSU(
            "kashuu_kiyomitsu",
            ToukenType.UCHIGATANA,
            1.65f
    ),
    KASEN_KANESADA(
            "kasen_kanesada",
            ToukenType.UCHIGATANA,
            1.75f
    ),
    MUTSUNOKAMI_YOSHIYUKI(
            "mutsunokami_yoshiyuki",
            ToukenType.UCHIGATANA,
            1.74f
    ),
    YAMANBAGIRI_KUNIHIRO(
            "yamanbagiri_kunihiro",
            ToukenType.UCHIGATANA,
            1.72f
    ),
    HACHISUKA_KOTETSU(
            "hachisuka_kotetsu",
            ToukenType.UCHIGATANA,
            1.76f
    );

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
