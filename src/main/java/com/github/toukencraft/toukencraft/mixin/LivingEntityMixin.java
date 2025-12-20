package com.github.toukencraft.toukencraft.mixin;

import com.github.toukencraft.toukencraft.entity.ToukenEntity;
import com.github.toukencraft.toukencraft.item.equipment.TousouItem;
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
        if (!(self instanceof ToukenEntity)) {
            var itemStack = self.getItemBySlot(EquipmentSlot.CHEST);
            if (itemStack.getItem() instanceof TousouItem) {
                self.spawnAtLocation(itemStack);
                self.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
            }
        }
    }
}
