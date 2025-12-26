package com.github.toukencraft.toukencraft.menu;

import com.github.toukencraft.toukencraft.ToukenCraft;
import com.github.toukencraft.toukencraft.datagen.ToukenCraftItemTagProvider;
import com.github.toukencraft.toukencraft.entity.ToukenEntity;
import com.github.toukencraft.toukencraft.init.ToukenCraftMenus;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ArmorSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.ticks.ContainerSingleItem;
import org.jetbrains.annotations.NotNull;


/** 刀剣男士を右クリックしたときに開く画面の挙動 */
public class ToukenGuiMenu extends AbstractContainerMenu {
    private static final ResourceLocation TOUSOU_SLOT_SPRITE = ResourceLocation.fromNamespaceAndPath(ToukenCraft.MOD_ID, "item/empty_armor_slot_tousou");

    private final Container toukenContainer;

    public final ToukenEntity toukenEntity;

    public final Player player;

    // クライアントサイドで呼び出し
    public ToukenGuiMenu(int containerId, Inventory playerInventory, ToukenGuiData data) {
        this(containerId, playerInventory, (ToukenEntity) playerInventory.player.level().getEntity(data.entityId()));
    }

    // サーバーサイドで呼び出し
    public ToukenGuiMenu(int containerId, Inventory playerInventory, ToukenEntity toukenEntity) {
        super(ToukenCraftMenus.TOUKEN_INVENTORY, containerId);

        this.toukenEntity = toukenEntity;
        this.player = playerInventory.player;
        this.toukenContainer = toukenEntity != null ? toukenEntity.inventory : new SimpleContainer(15);

        this.toukenContainer.startOpen(playerInventory.player);
        addSlots(playerInventory);
    }

    protected void addSlots(Inventory playerInventory) {
        // region 定数
        /* スロットのサイズ(w/h) */
        final int slotSize = 18;

        /* 左端のスロットを開始するx座標 */
        final int xOffset1 = 8;
        /* 刀剣男士の装備を開始するx座標 */
        final int xOffset2 = xOffset1 + slotSize * 3;
        /* 刀剣男士のインベントリを開始するx座標 */
        final int xOffset3 = xOffset2 + slotSize;

        /* 刀剣のスロット類を開始するy座標 */
        final int yOffset1 = 18;
        /* プレイヤーのインベントリを開始するy座標 */
        final int yOffset2 = 84;
        /* プレイヤーのホットバーを開始するy座標 */
        final int yOffset3 = 142;

        /* 刀剣男士のインベントリの行数*/
        final int nRowToukenInventory = 3;
        /* 刀剣男士のインベントリの列数 */
        final int nColToukenInventory = 5;

        /* プレイヤーのインベントリの行数 */
        final int nRowPlayerInventory = 3;
        /* プレイヤーのインベントリの列数 */
        final int nColPlayerInventory = 9;
        // endregion

        // 刀剣男士のインベントリ (インデックス : 0 ~ 14)
        for (var row = 0; row < nRowToukenInventory; row++) {
            for (var col = 0; col < nColToukenInventory; col++) {
                final int slotId = row * nColToukenInventory + col;
                this.addSlot(new Slot(toukenContainer, slotId, xOffset3 + slotSize * col, yOffset1 + slotSize * row));
            }
        }

        // プレイヤーのインベントリ (インデックス : 15 ~ 41)
        for (var row = 0; row < nRowPlayerInventory; row++) {
            for (var col = 0; col < nColPlayerInventory; col++) {
                final int slotId = 9 + row * nColPlayerInventory + col;
                this.addSlot(new Slot(playerInventory, slotId, xOffset1 + slotSize * col, yOffset2 + slotSize * row));
            }
        }

        // プレイヤーのホットバー (インデックス : 42 ~ 50)
        for (var col = 0; col < nColPlayerInventory; col++) {
            final int slotId = col;
            this.addSlot(new Slot(playerInventory, slotId, xOffset1 + slotSize * col, yOffset3));
        }

        //　刀剣スロット (操作不能) (インデックス : 51)
        this.addSlot(new Slot(new SimpleContainer(toukenEntity.getMainHandItem()), 0, xOffset1, yOffset1) {
            @Override
            public boolean mayPlace(ItemStack itemStack) {
                return false;
            }

            @Override
            public boolean mayPickup(Player player) {
                return false;
            }
        });

        // 装備スロット (インデックス : 52)
        {
            var container = new ContainerSingleItem() {
                @Override
                public @NotNull ItemStack getTheItem() {
                    return toukenEntity.getItemBySlot(EquipmentSlot.CHEST);
                }

                @Override
                public void setTheItem(ItemStack item) {
                    toukenEntity.setItemSlot(EquipmentSlot.CHEST, item);
                }

                @Override
                public void setChanged() {
                }

                @Override
                public boolean stillValid(Player player) {
                    return player.canInteractWithEntity(toukenEntity, 4.0);
                }
            };
            this.addSlot(new ArmorSlot(
                    container,
                    toukenEntity,
                    EquipmentSlot.CHEST,
                    0, xOffset1,
                    yOffset1 + slotSize,
                    TOUSOU_SLOT_SPRITE
            ) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.is(ToukenCraftItemTagProvider.TOUSOU_ITEMS);
                }
            });
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return toukenContainer.stillValid(player) &&
                toukenEntity.isAlive() &&
                player.canInteractWithEntity(toukenEntity, 4.0);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        var slot = slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        var srcStack = slot.getItem();
        var clonedStack = srcStack.copy();

        final int tInventoryStartIdx = 0;
        final int pInventoryStartIdx = 15;
        final int pHotBarStartIndex = 42;
        final int tEquipmentStartIndex = 51;
        final int outOfRangeIndex = 53;

        var success = true;

        if (index < pInventoryStartIdx || tEquipmentStartIndex <= index) {
            // 刀剣男士のインベントリ(装備含む) → プレイヤーのインベントリ(ホットバー含む)
            success = moveItemStackTo(srcStack, pInventoryStartIdx, tEquipmentStartIndex, true);

            // プレイヤーのインベントリが満杯のときは、刀剣男士のインベントリに動かす？
        } else label: {
            // プレイヤーのインベントリ(ホットバー含む) → 刀剣男士の装備欄
            success = moveItemStackTo(srcStack, tEquipmentStartIndex, outOfRangeIndex, false);
            if (success) {
                break label;
            }

            // プレイヤーのインベントリ(ホットバー含む) → 刀剣男士のインベントリ
            success = moveItemStackTo(srcStack, tInventoryStartIdx, pInventoryStartIdx, false);
            if (success) {
                break label;
            }

            // プレイヤーのインベントリ内で移動を試みる
            if (index < pHotBarStartIndex) {
                // インベントリ → ホットバー
                success = moveItemStackTo(srcStack, pHotBarStartIndex, tEquipmentStartIndex, false);
            } else {
                // ホットバー → インベントリ
                success = moveItemStackTo(srcStack, pInventoryStartIdx, pHotBarStartIndex, false);
            }
        }

        if (success) {
            if (srcStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            return clonedStack;
        } else {
            return ItemStack.EMPTY;
        }
    }
}
