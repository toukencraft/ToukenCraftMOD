package com.github.toukencraft.toukencraft.init;

import com.github.toukencraft.toukencraft.ToukenCraft;
import com.github.toukencraft.toukencraft.data.ToukenEnum;
import com.github.toukencraft.toukencraft.entity.ToukenEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.HashMap;


/** 刀剣男士を登録するクラス */
public class ToukenCraftEntities {
    /** 登録された刀剣男士の一覧 */
    public static HashMap<ToukenEnum, EntityType<ToukenEntity>> TOUKEN_DANSHI = new HashMap<>();

    private static void load_touken(ToukenEnum toukenEnum) {
        var property = toukenEnum.property;
        var entity = Registry.register(
                BuiltInRegistries.ENTITY_TYPE,
                new ResourceLocation(ToukenCraft.MOD_ID, property.entityIdentifier()),
                FabricEntityTypeBuilder
                        .create(MobCategory.CREATURE, ToukenEntity::new)
                        .dimensions(new EntityDimensions(property.height()/3, property.height(), true))
                        .trackRangeBlocks(64)
                        .forceTrackedVelocityUpdates(true)
                        .trackedUpdateRate(3)
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
