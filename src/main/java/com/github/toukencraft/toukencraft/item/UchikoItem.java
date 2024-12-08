package com.github.toukencraft.toukencraft.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;


/** 打粉 */
public class UchikoItem extends Item implements Vanishable {
    public UchikoItem() {
        super(new Item.Properties().durability(100));
    }

    @Override
    public int getEnchantmentValue() {
        return 2;
    }

    @Override
    public float getDestroySpeed(ItemStack itemStack, BlockState blockState) {
        return 1f;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity entity) {
        stack.hurtAndBreak(1, entity, i -> i.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity entity, LivingEntity sourceEntity) {
        stack.hurtAndBreak(2, sourceEntity, i -> i.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        if (equipmentSlot != EquipmentSlot.MAINHAND) {
            return super.getDefaultAttributeModifiers(equipmentSlot);
        }
        return ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .putAll(super.getDefaultAttributeModifiers(equipmentSlot))
                .put(
                        Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(
                                BASE_ATTACK_DAMAGE_UUID,
                                "Tool modifier",
                                -1f,
                                AttributeModifier.Operation.ADDITION
                        )
                )
                .put(
                        Attributes.ATTACK_SPEED,
                        new AttributeModifier(
                                BASE_ATTACK_SPEED_UUID,
                                "Tool modifier",
                                -3,
                                AttributeModifier.Operation.ADDITION
                        )
                )
                .build();
    }
}
