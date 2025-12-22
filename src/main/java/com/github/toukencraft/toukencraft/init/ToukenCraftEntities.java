package com.github.toukencraft.toukencraft.init;

import com.github.toukencraft.toukencraft.ToukenCraft;
import com.github.toukencraft.toukencraft.data.ToukenEnum;
import com.github.toukencraft.toukencraft.entity.ToukenEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.HashMap;
import java.util.LinkedHashMap;


/** 刀剣男士を登録するクラス */
public class ToukenCraftEntities {
    /** 登録された刀剣男士の一覧 */
    public static HashMap<ToukenEnum, EntityType<ToukenEntity>> TOUKEN_DANSHI = new LinkedHashMap<>();

    private static void load_touken(ToukenEnum toukenEnum) {
        var property = toukenEnum.property;
        var entity = Registry.register(
                BuiltInRegistries.ENTITY_TYPE,
                ResourceLocation.fromNamespaceAndPath(ToukenCraft.MOD_ID, property.entityIdentifier()),
                EntityType.Builder.of(ToukenEntity::new, MobCategory.CREATURE)
                        .attach(EntityAttachment.VEHICLE, 0, 0.45f, 0)  // yを大きくすると乗り物に乗った時の位置が下へ
                        .build()
        );
        TOUKEN_DANSHI.put(toukenEnum, entity);
        FabricDefaultAttributeRegistry.register(entity, property.toAttribute());
    }

    public static void load() {
        for (var touken : ToukenEnum.values()) {
            load_touken(touken);
        }
    }
}
