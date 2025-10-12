package com.github.toukencraft.toukencraft.item.equipment;

import com.github.toukencraft.toukencraft.init.ToukenCraftEntities;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.Equippable;

public class TousouItem extends Item {
    public TousouItem(Properties properties) {
        super(properties);
    }

    public static Item.Properties properties(ArmorMaterial material) {
        // Item.Properties.humanoidArmorを参考
        var type = ArmorType.CHESTPLATE;
        var entities = ToukenCraftEntities.TOUKEN_DANSHI.values().toArray(new EntityType[0]);
        var equippable = Equippable.builder(ArmorType.CHESTPLATE.getSlot())
                .setAllowedEntities(entities) // 刀剣男士だけが装備できるように限定
                .setEquipSound(material.equipSound())
                .setAsset(material.assetId())
                .build();
        return new Item.Properties()
                .durability(type.getDurability(material.durability()))
                .attributes(material.createAttributes(type))
                .enchantable(material.enchantmentValue())
                .component(DataComponents.EQUIPPABLE, equippable)
                .repairable(material.repairIngredient());
    }
}
