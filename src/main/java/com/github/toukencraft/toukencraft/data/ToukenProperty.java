package com.github.toukencraft.toukencraft.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ToolMaterial;


/**
 * 刀剣男士の情報
 * @param itemIdentifier 刀剣のidentifier
 * @param entityIdentifier 刀剣男士のidentifier
 * @param textureLocation 刀剣男士のテクスチャの場所
 * @param toukenType 刀種
 * @param height 刀剣男士の身長[m]
 */
public record ToukenProperty(
        String itemIdentifier,
        String entityIdentifier,
        ResourceLocation textureLocation,
        ToukenType toukenType,
        float height
) {
    /** Entityのパラメータ */
    public AttributeSupplier.Builder toAttribute() {
        // TODO 刀剣 or 刀種ごとに変更？
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20 * 2)  // 生存
                .add(Attributes.ATTACK_DAMAGE, 3)  // 打撃
                .add(Attributes.ARMOR, 4)  // 統率
                .add(Attributes.MOVEMENT_SPEED, 0.25f)  // 機動
                .add(Attributes.FOLLOW_RANGE, 16)
                ;
    }

    public ToolMaterial toToolMaterial() {
        return new ToolMaterial(
                BlockTags.INCORRECT_FOR_IRON_TOOL,  // 採掘不能ブロック
                ToolMaterial.DIAMOND.durability(),  // 耐久
                ToolMaterial.IRON.speed(),  // 採掘速度
                ToolMaterial.IRON.attackDamageBonus(),  // 攻撃倍率
                ToolMaterial.IRON.enchantmentValue(),  // エンチャント適正
                ItemTags.IRON_TOOL_MATERIALS  // 修復用アイテム
        );
    }
}
