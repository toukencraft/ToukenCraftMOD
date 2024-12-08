package com.github.toukencraft.toukencraft.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;


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

    /** Itemのパラメータ */
    public Tier toTier() {
        return new Tier() {
            /** 耐久 */
            @Override
            public int getUses() {
                return Tiers.DIAMOND.getUses();
            }

            /** 攻撃倍率 */
            @Override
            public float getAttackDamageBonus() {
                return Tiers.DIAMOND.getAttackDamageBonus();
            }


            /** 採掘効率 */
            @Override
            public float getSpeed() {
                return Tiers.IRON.getSpeed();
            }

            /** 採掘レベル */
            @Override
            public int getLevel() {
                return Tiers.IRON.getLevel();
            }

            /** エンチャント適正 */
            @Override
            public int getEnchantmentValue() {
                return Tiers.IRON.getEnchantmentValue();
            }

            /** 修復素材 */
            @Override
            public @NotNull Ingredient getRepairIngredient() {
                return Ingredient.of(new ItemStack(Items.IRON_INGOT));
            }
        };
    }
}
