package com.github.toukencraft.toukencraft.client.gui;

import com.github.toukencraft.toukencraft.ToukenCraft;
import com.github.toukencraft.toukencraft.entity.ToukenEntity;
import com.github.toukencraft.toukencraft.menu.ToukenGuiMenu;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;


/** 刀剣男士を右クリックしたときに開く画面 */
@Environment(EnvType.CLIENT)
public class ToukenGuiScreen extends AbstractContainerScreen<ToukenGuiMenu> {
    private static final ResourceLocation BACKGROUND_LOCATION =
            ResourceLocation.fromNamespaceAndPath(ToukenCraft.MOD_ID, "textures/gui/container/inventory.png");

    private final ToukenEntity entity;

    public ToukenGuiScreen(ToukenGuiMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.entity = menu.toukenEntity;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        var offsetX = (this.width - this.imageWidth) / 2;
        var offsetY = (this.height - this.imageHeight) / 2;

        guiGraphics.blit(BACKGROUND_LOCATION, offsetX, offsetY, 0, 0, this.imageWidth, this.imageHeight);

        if (entity != null) {
            /* スロットのサイズ(w/h) */
            final int slotSize = 18;
            /* 左端のスロットを開始するx座標 */
            final int xOffset1 = 8;
            /* 刀剣のスロット類を開始するy座標 */
            final int yOffset1 = 18;

            InventoryScreen.renderEntityInInventoryFollowsMouse(
                    guiGraphics,
                    offsetX + xOffset1 + slotSize,
                    offsetY + yOffset1,
                    offsetX + xOffset1 + 3 * slotSize - 2,
                    offsetY + yOffset1 + 3 * slotSize - 2,
                    20,
                    0f,
                    mouseX,
                    mouseY,
                    entity
            );
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
