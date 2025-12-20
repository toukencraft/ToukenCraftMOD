package com.github.toukencraft.toukencraft.entity;

import com.github.toukencraft.toukencraft.util.ParticleUtil;
import com.github.toukencraft.toukencraft.ToukenCraft;
import com.github.toukencraft.toukencraft.init.ToukenCraftItems;
import com.github.toukencraft.toukencraft.menu.ToukenGuiMenu;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/** 刀剣男士 */
public class ToukenEntity extends TamableAnimal {
    public final SimpleContainer inventory;

    public ToukenEntity(EntityType<? extends ToukenEntity> type, Level level) {
        super(type, level);

        xpReward = 0;
        inventory = new SimpleContainer(15);
        inventory.addListener(container -> {
            // インベントリの変化を即座に刀剣のNBTに反映 (インベントリのGUIで齟齬が生じないようにするため)
            var toukenItemStack =  getToukenItemStack();
            var nbt = serializeNBT();
            toukenItemStack.setTag(nbt);
        });

        setNoAi(false);
        setPersistenceRequired();  // 離れてもデスポーンさせない(?)
        setMaxUpStep(0.6f);
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
    public boolean removeWhenFarAway(double d) {
        return false;
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return false;
    }

    @Override
    public double getMyRidingOffset() {
        return -0.14;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return BuiltInRegistries.SOUND_EVENT.get(new ResourceLocation("entity.generic.hurt"));
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return BuiltInRegistries.SOUND_EVENT.get(new ResourceLocation("entity.item.break"));
    }

    @Override
    protected void registerGoals() {
        // オオカミの挙動を参考

        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.2, false) {
            @Override
            protected double getAttackReachSqr(LivingEntity attackTarget) {
                return 4.0F + attackTarget.getBbWidth();  // NOTE 攻撃のリーチ
            }
        });
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1, 10, 2, false));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new OwnerHurtTargetGoal(this));  // 審神者が攻撃した相手を攻撃
        this.targetSelector.addGoal(2, new OwnerHurtByTargetGoal(this));  // 審神者を攻撃した相手を攻撃
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this).setAlertOthers());  // 自身を攻撃した相手を攻撃
        this.targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, AbstractSkeleton.class, false));
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
    protected void dropEquipment() {
        super.dropEquipment();
        for (var itemStack : inventory.items) {
            if (!itemStack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemStack)) {
                spawnAtLocation(itemStack);
            }
        }
        {
            var itemStack = getItemBySlot(EquipmentSlot.CHEST);
            if (!itemStack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemStack)) {
                spawnAtLocation(itemStack);
            }
        }
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, InteractionHand hand) {
        var itemStackInHand = player.getItemInHand(hand);
        var result = InteractionResult.sidedSuccess(level().isClientSide);

        if (itemStackInHand.getItem() == Items.NAME_TAG) {
            // TODO 他プレイヤーの刀剣男士に名札は使用できなくする

            var touken = getToukenItemStack();
            if (hasCustomName()) {
                touken.setHoverName(getCustomName());
            } else {
                touken.resetHoverName();
            }
        }

        if (getOwner() != player) {
            return result;  // 自分の刀剣ではない
        }

        if (itemStackInHand.getItem() == ToukenCraftItems.UCHIKO) {
            useUchiko(player, hand);
        } else if (player.isShiftKeyDown()) {
            openCustomInventoryScreen(player);
        } else if (itemStackInHand.getItem() == Items.AIR) {
            unsummon(player, hand);
        }

        return result;
    }

    /** 打粉で回復 */
    protected void useUchiko(Player player, InteractionHand hand) {
        var uchikoItemStack = player.getItemInHand(hand);
        var level = level();
        var p = position();

        if (level.isClientSide) {
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
        uchikoItemStack.hurtAndBreak(1, player, entity -> entity.broadcastBreakEvent(hand));

        if (level.isClientSide) {
            if (getHealth() < getMaxHealth()) {
                // TODO パーティクルの継続時間を短くする (連続使用した時分かりにくい)
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
            // TODO インベントリを開いている間に行ってしまわないようにする
            serverPlayer.openMenu(new ExtendedScreenHandlerFactory() {
                @Override
                public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf packetBuffer) {
                    packetBuffer.writeVarInt(ToukenEntity.this.getId());
                }

                @Override
                public @NotNull Component getDisplayName() {
                    return ToukenEntity.this.getDisplayName();
                }

                @Override
                public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
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

        // インベントリのアイテムを刀剣のNBTに保存
        toukenItemStack.setTag(serializeNBT());

        // 名札などで名前が付けられていれば反映する
        if (hasCustomName()) {
            toukenItemStack.setHoverName(getCustomName());
        }

        // 刀剣男士のHPを刀剣の耐久値に反映
        syncHealthToDurability();

        player.setItemInHand(hand, toukenItemStack);  // 刀剣をプレイヤーの手に移動
        remove(RemovalReason.DISCARDED);  // 刀剣男士の顕現を解除
    }

    private CompoundTag serializeNBT() {
        var nbtTagList = new ListTag();

        for (var i = 0; i < this.inventory.getContainerSize(); i++) {
            var itemStack = this.inventory.getItem(i);
            if (!itemStack.isEmpty()) {
                var itemTag = new CompoundTag();
                itemTag.putInt("Slot", i);
                itemStack.save(itemTag);
                nbtTagList.add(itemTag);
            }
        }
        {
            var tousou = this.getItemBySlot(EquipmentSlot.CHEST);
            if (!tousou.isEmpty()) {
                var itemTag = new CompoundTag();
                itemTag.putInt("Slot", this.inventory.getContainerSize());
                tousou.save(itemTag);
                nbtTagList.add(itemTag);
            }
        }

        var nbt = getToukenItemStack().getTag();
        if (nbt == null) {
            nbt = new CompoundTag();
        }
        nbt.put("Items", nbtTagList);
        nbt.putInt("Size", this.inventory.items.size());
        return nbt;
    }

    public void deserializeNBT(CompoundTag tag) {
        if (tag == null) {
            return;
        }

        var tagList = tag.getList("Items", Tag.TAG_COMPOUND);
        for (var i = 0; i < tagList.size(); i++) {
            var itemTag = tagList.getCompound(i);
            var slotIndex = itemTag.getInt("Slot");
            if (0 <= slotIndex && slotIndex < inventory.items.size()) {
                inventory.items.set(slotIndex, ItemStack.of(itemTag));
            } else if (slotIndex == inventory.items.size()) {
                this.setItemSlot(EquipmentSlot.CHEST, ItemStack.of(itemTag));
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("Inventory", serializeNBT());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        deserializeNBT(compound.getCompound("Inventory"));
    }
}
