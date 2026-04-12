package com.github.toukencraft.toukencraft.datagen;

import com.github.toukencraft.toukencraft.ToukenCraft;
import com.github.toukencraft.toukencraft.init.ToukenCraftEntities;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;


/** EntityTypeにタグを追加するクラス */
public class ToukenCraftEntityTypeTagProvider extends FabricTagsProvider.EntityTypeTagsProvider {
    /** 刀剣男士のタグ */
    public static final TagKey<EntityType<?>> DANSHI_ENTITY_TYPES = TagKey.create(
            Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(ToukenCraft.MOD_ID, "danshi")
    );

    public ToukenCraftEntityTypeTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        valueLookupBuilder(DANSHI_ENTITY_TYPES).add(ToukenCraftEntities.TOUKEN_DANSHI.values().toArray(new EntityType[0]));
    }
}
