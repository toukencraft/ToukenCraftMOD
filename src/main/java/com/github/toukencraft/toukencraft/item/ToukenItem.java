package com.github.toukencraft.toukencraft.item;

import com.github.toukencraft.toukencraft.ToukenCraft;
import com.github.toukencraft.toukencraft.entity.ToukenEntity;
import com.github.toukencraft.toukencraft.init.ToukenCraftDataComponents;
import com.github.toukencraft.toukencraft.util.ParticleUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


/** 刀剣 */
public class ToukenItem extends Item {
    /** 対応する刀剣男士 */
    protected final EntityType<ToukenEntity> entityType;

    public ToukenItem(
            Properties properties,
            EntityType<ToukenEntity> entityType
    ) {
        super(properties);
        this.entityType = entityType;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
        summon(useOnContext);
        return InteractionResult.PASS;
    }

    protected void summon(UseOnContext context) {
        var player = context.getPlayer();
        if (player == null) {
            return;
        }
        var level = context.getLevel();
        var toukenItemStack = context.getItemInHand();
        var summonPos = context.getClickedPos().offset(context.getClickedFace().getUnitVec3i());

        // TODO 窒息チェック

        if (level instanceof ServerLevel serverLevel) {
            // 刀剣男士を顕現

            var entity = entityType.create(
                    serverLevel, null, summonPos,
                    EntitySpawnReason.MOB_SUMMONED, true, true
            );
            if (entity == null) {
                ToukenCraft.LOGGER.debug("Summon failed");
                return;
            }

            // TODO 刀剣と刀剣男士をUUIDで紐づける場合はここで

            // プレイヤーの方を向く
            var xRot = 0;
            var yRot = player.getYRot() + 180;
            entity.setXRot(xRot);
            entity.setYRot(yRot);
            entity.setYHeadRot(yRot);
            entity.setYBodyRot(yRot);
            entity.xRotO = xRot;
            entity.yRotO = yRot;
            entity.yHeadRotO = yRot;
            entity.yBodyRotO = yRot;

            // 金床などで名前が付けられていれば反映
            entity.setCustomName(toukenItemStack.getCustomName());

            // インベントリのアイテムを復元
            var contents = toukenItemStack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
            contents.copyInto(entity.inventory.items);
            var items = contents.stream().toList();
            if (items.size() >= entity.inventory.getContainerSize() && entity.inventory.getContainerSize() < items.size()) {
                entity.setItemSlot(EquipmentSlot.CHEST, items.get(entity.inventory.getContainerSize()));
            }

            // タグを復元
            for (var tag : toukenItemStack.getOrDefault(ToukenCraftDataComponents.TOUKEN_TAG, new ArrayList<String>())) {
                entity.addTag(tag);
            }

            // 刀剣を渡す
            player.setItemInHand(context.getHand(), ItemStack.EMPTY);
            entity.setLeftHanded(false);  // 確率で左利きになる(?)のを防ぐ
            entity.setItemInHand(InteractionHand.MAIN_HAND, toukenItemStack);
            serverLevel.addFreshEntity(entity);

            // 刀剣の耐久度を刀剣男士のHPに反映
            int itemMaxHp = toukenItemStack.getMaxDamage();
            int itemHp = itemMaxHp - toukenItemStack.getDamageValue();
            float entityMaxHp = entity.getMaxHealth();
            float entityHp = Math.max(entityMaxHp * ((float) itemHp / itemMaxHp), 1);
            entity.setHealth(entityHp);

            // プレイヤーと紐づけ
            entity.tame(player);
        } else if (level instanceof ClientLevel clientLevel) {
            // 桜吹雪のパーティクルを再生
            // TODO 身長を加味するように

            var upperPos = summonPos.above().getCenter();
            ParticleUtil.addParticle(
                    clientLevel,
                    ParticleTypes.CHERRY_LEAVES, 20,
                    upperPos.x, upperPos.y, upperPos.z,
                    1.5, 1.5, 1.5,
                    0, 2, 0
            );
        }
    }
}
