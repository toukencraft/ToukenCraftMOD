package com.github.toukencraft.toukencraft.mixin;

import com.github.toukencraft.toukencraft.datagen.ToukenCraftEntityTypeTagProvider;
import com.github.toukencraft.toukencraft.datagen.ToukenCraftItemTagProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo info) {
        // 刀剣男士以外が刀装を装備していたらドロップする
        var self = (LivingEntity) (Object) this;
        if (!(self.getType().is((ToukenCraftEntityTypeTagProvider.DANSHI_ENTITY_TYPES)))) {
            var itemStack = self.getItemBySlot(EquipmentSlot.CHEST);
            if (itemStack.is(ToukenCraftItemTagProvider.TOUSOU_ITEMS)) {
                self.spawnAtLocation(itemStack);
                self.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
            }
        }
    }
}
