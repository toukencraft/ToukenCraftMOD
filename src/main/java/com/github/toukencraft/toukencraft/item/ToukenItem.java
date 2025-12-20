package com.github.toukencraft.toukencraft.item;

import com.github.toukencraft.toukencraft.ToukenCraft;
import com.github.toukencraft.toukencraft.entity.ToukenEntity;
import com.github.toukencraft.toukencraft.util.ParticleUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;


/** 刀剣 */
public class ToukenItem extends SwordItem {
    /** 対応する刀剣男士 */
    protected final EntityType<ToukenEntity> entityType;

    public ToukenItem(Tier tier, EntityType<ToukenEntity> entityType) {
        this(tier, new Properties(), entityType);
    }

    public ToukenItem(
            Tier tier,
            Properties properties,
            EntityType<ToukenEntity> entityType
    ) {
        super(tier, properties);
        this.entityType = entityType;
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            TooltipContext tooltipContext,
            List<Component> tooltipComponents,
            TooltipFlag isAdvanced
    ) {
        super.appendHoverText(stack, tooltipContext, tooltipComponents, isAdvanced);

        var textCapacity = 4;
        var contents = stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).nonEmptyItems();

        var addedNum = 0;
        var remainingNum = 0;
        for (var content : contents) {
            if (addedNum < textCapacity) {
                tooltipComponents.add(Component.translatable("container.shulkerBox.itemCount", content.getHoverName(), content.getCount()));
                addedNum++;
            } else {
                remainingNum++;
            }
        }

        if (remainingNum > 0) {
            tooltipComponents.add(Component.translatable("container.shulkerBox.more", remainingNum).withStyle(ChatFormatting.ITALIC));
        }
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
        var result = super.useOn(useOnContext);

        summon(useOnContext);

        return result;
    }

    /** 刀剣男士を顕現する */
    protected void summon(UseOnContext context) {
        var level = context.getLevel();
        var player = context.getPlayer();
        var clickedPos = context.getClickedPos();
        var clickedFace = context.getClickedFace();
        var hand = context.getHand();
        var toukenItemStack = context.getItemInHand();

        if (player == null) {
            return;
        }

        var pos = clickedPos.offset(clickedFace.getNormal());
        var upperPos = pos.above();

        // TODO 窒息チェック

        if (level instanceof ServerLevel serverLevel) {
            // 刀剣男士を顕現

            var entity = (ToukenEntity) entityType.create(
                    serverLevel, null, pos,
                    MobSpawnType.MOB_SUMMONED, true, true
            );

            if (entity == null) {
                ToukenCraft.LOGGER.warn("created entity is null");
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

            // 金床などで名前が付けられていれば反映する
            entity.setCustomName(toukenItemStack.get(DataComponents.CUSTOM_NAME));

            // インベントリのアイテムを復元
            var contents = toukenItemStack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
            contents.copyInto(entity.inventory.items);
            var items = contents.stream().toList();
            if (items.size() >= entity.inventory.getContainerSize()) {
                entity.setItemSlot(EquipmentSlot.CHEST, items.get(entity.inventory.getContainerSize()));
            }

            // 刀剣を渡す
            player.setItemInHand(hand, ItemStack.EMPTY);
            entity.setLeftHanded(false);  // 確率で左利きになる(?)のを防ぐ
            entity.setItemInHand(InteractionHand.MAIN_HAND, toukenItemStack);
            serverLevel.addFreshEntity(entity);

            // 刀剣の耐久度を刀剣男士のHPに反映
            int itemMaxHp = toukenItemStack.getMaxDamage();
            int itemHp = itemMaxHp - toukenItemStack.getDamageValue();
            float entityMaxHp = entity.getMaxHealth();
            float entityHp = Math.max(entityMaxHp * ((float) itemHp / itemMaxHp), 1);
            entity.setHealth(entityHp);
            ToukenCraft.LOGGER.info(String.format("summon: (%d / %d) -> (%.1f / %.1f)", itemHp, itemMaxHp, entityHp, entityMaxHp));

            // プレイヤーと紐づけ
            entity.tame(player);
        } else if (level instanceof ClientLevel clientLevel) {
            // 桜吹雪のパーティクルを再生
            // FIXME 身長を加味するように
            ParticleUtil.addParticle(
                    clientLevel,
                    ParticleTypes.CHERRY_LEAVES, 20,
                    upperPos.getX() + 0.5, upperPos.getY() + 0.5, upperPos.getZ() + 0.5,
                    1.5, 1.5, 1.5,
                    0, 2, 0
            );
        }
    }
}
