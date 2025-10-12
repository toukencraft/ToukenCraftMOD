package com.github.toukencraft.toukencraft.entity;

import com.github.toukencraft.toukencraft.menu.ToukenGuiData;
import com.github.toukencraft.toukencraft.util.ParticleUtil;
import com.github.toukencraft.toukencraft.init.ToukenCraftItems;
import com.github.toukencraft.toukencraft.menu.ToukenGuiMenu;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/** 刀剣男士 */
public class ToukenEntity extends TamableAnimal implements InventoryCarrier {
    public final SimpleContainer inventory;

    @Override
    public @NotNull SimpleContainer getInventory() {
        return inventory;
    }

    public ToukenEntity(EntityType<? extends ToukenEntity> type, Level level) {
        super(type, level);

        xpReward = 0;
        inventory = new SimpleContainer(15);
        inventory.addListener(container -> {
            // インベントリの変化を即座に刀剣のNBTに反映 (インベントリのGUIで齟齬が生じないようにするため)
            var toukenItemStack = getToukenItemStack();
            toukenItemStack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(inventory.items));
        });

        setNoAi(false);
        setPersistenceRequired();  // 離れてもデスポーンしないように
    }

    protected ItemStack getToukenItemStack() {
        return this.getMainHandItem();  // TODO 畑当番などを実装すると、刀剣がメインハンドにないケースも考えられる
    }

    @Override
    public void setHealth(float health) {
        super.setHealth(health);
        try {
            syncHealthToDurability();
        } catch (NullPointerException e) {
            // NOTE 顕現直後の初期化時に呼ばれると、スロットがまだ初期化されていないためエラーが発生する
            // FIXME 初期化の時だけエラーを無視したいが、それ以外のときはログに残すなりしたい
        }
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ITEM_BREAK.value();
    }

    @Override
    protected void registerGoals() {
        // NOTE オオカミの挙動を参考

        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2, false));
        this.goalSelector.addGoal(3, new FollowOwnerGoal(this, 1, 10, 2));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractSkeleton.class, false));
    }

    @Override
    public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
        // オオカミの挙動を参考
        // 他プレイヤーの刀剣男士への攻撃は有効化していない

        if (target instanceof Creeper || target instanceof Ghast) {
            return false;
        }
        if (target instanceof Player && owner instanceof Player && !(((Player) owner).canHarmPlayer((Player) target))) {
            return false;
        }
        if (target instanceof AbstractHorse && ((AbstractHorse) target).isTamed()) {
            return false;
        }
        if (target instanceof TamableAnimal && ((TamableAnimal) target).isTame()) {
            return  false;
        }
        return super.wantsToAttack(target, owner);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        updateSwingTime();  // 攻撃モーションの再生に必要
    }

    @Override
    protected void dropEquipment(ServerLevel serverLevel) {
        super.dropEquipment(serverLevel);
        for (var itemStack : inventory.items) {
            if (!itemStack.isEmpty() && !EnchantmentHelper.has(itemStack, EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP)) {
                spawnAtLocation(serverLevel, itemStack);
            }
        }
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, InteractionHand hand) {
        var itemStackInHand = player.getItemInHand(hand);
        var itemInHand = itemStackInHand.getItem();

        if (itemInHand == Items.NAME_TAG) {
            // TODO 他プレイヤーの刀剣男士に名札を使用できなくする

            var touken = getToukenItemStack();
            if (hasCustomName()) {
                touken.set(DataComponents.CUSTOM_NAME, getCustomName());
            } else {
                touken.remove(DataComponents.CUSTOM_NAME);
            }
        }

        if (getOwner() != player) {
            return InteractionResult.SUCCESS;
        }

        if (itemInHand == ToukenCraftItems.UCHIKO) {
            useUchiko(player, hand);
        } else if (player.isShiftKeyDown()) {
            openCustomInventoryScreen(player);
        } else if (itemInHand == Items.AIR) {
            unsummon(player, hand);
        }

        return InteractionResult.SUCCESS;
    }

    /** 打粉で回復 */
    protected void useUchiko(Player player, InteractionHand hand) {
        var uchikoItemStack = player.getItemInHand(hand);
        var level = level();
        var p = position();

        if (level.isClientSide()) {
            // 打粉のパーティクル
            ParticleUtil.addParticle(
                    level,
                    ParticleTypes.FIREWORK, 1,
                    p.x, p.y + 1.5/2, p.z,
                    1.5, 1.5, 1.5,
                    0, 0, 0
            );
        }

        if (getHealth() >= getMaxHealth()) {
            return;  // 既に全快
        }

        setHealth(getHealth() + 1);
        uchikoItemStack.hurtAndBreak(1, player, hand);

        if (level.isClientSide()) {
            if (getHealth() < getMaxHealth()) {
                // TODO パーティクルの継続時間を短くする (連続で使用すると分かりにくいため)
                ParticleUtil.addParticle(
                        level,
                        ParticleTypes.HAPPY_VILLAGER, 1,
                        p.x, getEyeY(), p.z,
                        1.5, 1.5, 1.5,
                        0, 0, 0
                );
            } else {
                // HPが全快したら多めに出す
                ParticleUtil.addParticle(
                        level,
                        ParticleTypes.HAPPY_VILLAGER, 15,
                        p.x, getEyeY(), p.z,
                        2, 1.5, 2,
                        0, 0, 0
                );
            }
        }
    }

    /** インベントリを開く */
    protected void openCustomInventoryScreen(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            // TODO インベントリを開いている間に刀剣男士が行ってしまわないようにする
            serverPlayer.openMenu(new ExtendedScreenHandlerFactory<>() {
                @Override
                public ToukenGuiData getScreenOpeningData(ServerPlayer serverPlayer) {
                    return new ToukenGuiData(ToukenEntity.this.getId());
                }

                @Override
                public @NotNull Component getDisplayName() {
                    return ToukenEntity.this.getDisplayName();
                }

                @Override
                public @NotNull AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
                    return new ToukenGuiMenu(containerId, inventory, ToukenEntity.this);
                }
            });
        }
    }

    /** 刀剣男士のHPを刀剣の耐久値に反映 */
    private void syncHealthToDurability() {
        var toukenItemStack = getToukenItemStack();

        float entityMaxHp = getMaxHealth();
        float entityHp = getHealth();

        int itemMaxHp = toukenItemStack.getMaxDamage();
        int itemHp = Math.max(Math.round(itemMaxHp * entityHp / entityMaxHp), 1);

        toukenItemStack.setDamageValue(itemMaxHp - itemHp);
    }

    /** 顕現を解除する */
    protected void unsummon(Player player, InteractionHand hand) {
        if (player.getItemInHand(hand).getItem() != Items.AIR) {
            return;  // 手が空いていない
        }

        var toukenItemStack = getToukenItemStack();

        // 刀剣男士のHPを刀剣の耐久値に反映
        syncHealthToDurability();

        player.setItemInHand(hand, toukenItemStack);  // 刀剣をプレイヤーの手に移動
        remove(RemovalReason.DISCARDED);  // 刀剣男士の顕現を解除
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput value) {
        super.addAdditionalSaveData(value);
        this.writeInventoryToTag(value);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput value) {
        super.readAdditionalSaveData(value);
        this.readInventoryFromTag(value);
    }
}
