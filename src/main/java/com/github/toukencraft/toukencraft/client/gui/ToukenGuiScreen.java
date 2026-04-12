package com.github.toukencraft.toukencraft.client.gui;

import com.github.toukencraft.toukencraft.ToukenCraft;
import com.github.toukencraft.toukencraft.entity.ToukenEntity;
import com.github.toukencraft.toukencraft.menu.ToukenGuiMenu;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;


@Environment(EnvType.CLIENT)
public class ToukenGuiScreen extends AbstractContainerScreen<ToukenGuiMenu> {
    private static final Identifier BACKGROUND_LOCATION =
            Identifier.fromNamespaceAndPath(ToukenCraft.MOD_ID, "textures/gui/container/inventory.png");

    private final ToukenEntity entity;

    public ToukenGuiScreen(ToukenGuiMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.entity = menu.toukenEntity;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float a) {
        super.extractBackground(guiGraphics, mouseX, mouseY, a);

        var offsetX = (this.width - this.imageWidth) / 2;
        var offsetY = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED,
                BACKGROUND_LOCATION,
                offsetX, offsetY,
                0, 0,
                this.imageWidth, this.imageHeight,
                256, 256
        );

        if (entity != null) {
            /* スロットのサイズ(w/h) */
            final int slotSize = 18;
            /* 左端のスロットを開始するx座標 */
            final int xOffset1 = 8;
            /* 刀剣のスロット類を開始するy座標 */
            final int yOffset1 = 18;

            InventoryScreen.extractEntityInInventoryFollowsMouse(
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
}
