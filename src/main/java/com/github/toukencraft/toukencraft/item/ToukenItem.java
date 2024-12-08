package com.github.toukencraft.toukencraft.item;

import com.github.toukencraft.toukencraft.util.ParticleUtil;
import com.github.toukencraft.toukencraft.ToukenCraft;
import com.github.toukencraft.toukencraft.entity.ToukenEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;


/** 刀剣 */
public class ToukenItem extends SwordItem {
    /** 対応する刀剣男士 */
    protected final EntityType<ToukenEntity> entityType;

    public ToukenItem(Tier tier, EntityType<ToukenEntity> entityType) {
        this(tier, 3, -2.4f, new Properties(), entityType);
    }

    public ToukenItem(
            Tier tier,
            int attackDamageModifier,
            float attackSpeedModifier,
            Properties properties,
            EntityType<ToukenEntity> entityType
    ) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
        this.entityType = entityType;
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            Level level,
            List<Component> tooltipComponents,
            TooltipFlag isAdvanced
    ) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);

        var tag = stack.getTag();
        if (tag != null && tag.contains("Items")) {
            var tagList = tag.getList("Items", Tag.TAG_COMPOUND);
            final int maxSize = 4;

            for (var i = 0; i < Math.min(maxSize, tagList.size()); i++) {
                var itemTag = tagList.getCompound(i);
                var itemStack = ItemStack.of(itemTag);
                // NOTE https://bugs.mojang.com/browse/MC-248778
                var component = itemStack.getHoverName().copy().append(" x").append(String.valueOf(itemStack.getCount()));
                tooltipComponents.add(component);
            }

            if (tagList.size() > maxSize) {
                tooltipComponents.add(Component.translatable(
                        "container.shulkerBox.more",
                        tagList.size() - maxSize
                ).withStyle(ChatFormatting.ITALIC));
            }
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

        if (level instanceof ClientLevel clientLevel) {
            // 桜吹雪のパーティクルを再生
            // FIXME 身長を加味するように
            ParticleUtil.addParticle(
                    clientLevel,
                    ParticleTypes.CHERRY_LEAVES, 20,
                    upperPos.getX() + 0.5, upperPos.getY() + 0.5, upperPos.getZ() + 0.5,
                    1.5, 1.5, 1.5,
                    0, 2, 0
            );
        } else if (level instanceof ServerLevel serverLevel) {
            // 刀剣男士を顕現

            var entity = (ToukenEntity) entityType.create(
                    serverLevel, null, null, pos,
                    MobSpawnType.MOB_SUMMONED, true, true
            );

            if (entity == null) {
                ToukenCraft.LOGGER.warn("created entity is null");
                return;
            }

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
            if (toukenItemStack.hasCustomHoverName()) {
                entity.setCustomName(toukenItemStack.getHoverName());
            }

            // NBTからインベントリのアイテムを復元
            var tag = toukenItemStack.getTag();
            entity.deserializeNBT(tag);

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
        }
    }
}
