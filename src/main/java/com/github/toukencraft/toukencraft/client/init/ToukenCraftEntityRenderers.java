package com.github.toukencraft.toukencraft.client.init;

import com.github.toukencraft.toukencraft.client.renderer.ToukenEntityRenderer;
import com.github.toukencraft.toukencraft.data.ToukenEnum;
import com.github.toukencraft.toukencraft.init.ToukenCraftEntities;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;


/** 刀剣男士の描画を行うRendererを登録するクラス */
public class ToukenCraftEntityRenderers {
    private static void load_touken(ToukenEnum toukenEnum) {
        EntityRendererRegistry.register(
                ToukenCraftEntities.TOUKEN_DANSHI.get(toukenEnum),
                context -> new ToukenEntityRenderer(context, toukenEnum.property.textureLocation(), toukenEnum.property.height())
        );
    }

    public static void load() {
        for (var touken : ToukenEnum.values()) {
            load_touken(touken);
        }
    }
}
