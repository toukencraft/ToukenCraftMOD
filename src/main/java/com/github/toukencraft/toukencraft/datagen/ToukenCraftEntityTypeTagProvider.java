package com.github.toukencraft.toukencraft.datagen;

import com.github.toukencraft.toukencraft.ToukenCraft;
import com.github.toukencraft.toukencraft.init.ToukenCraftEntities;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;


/** EntityTypeにタグを追加するクラス */
public class ToukenCraftEntityTypeTagProvider extends FabricTagProvider.EntityTypeTagProvider {
    /** 刀剣男士のタグ */
    public static final TagKey<EntityType<?>> DANSHI_ENTITY_TYPES = TagKey.create(
            Registries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(ToukenCraft.MOD_ID, "danshi")
    );

    public ToukenCraftEntityTypeTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        getOrCreateTagBuilder(DANSHI_ENTITY_TYPES).add(ToukenCraftEntities.TOUKEN_DANSHI.values().toArray(new EntityType[0]));
    }
}
