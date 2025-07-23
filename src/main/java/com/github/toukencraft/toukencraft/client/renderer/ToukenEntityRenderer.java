package com.github.toukencraft.toukencraft.client.renderer;

import com.github.toukencraft.toukencraft.entity.ToukenEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;


/** 刀剣男士のRenderer */
@Environment(EnvType.CLIENT)
public class ToukenEntityRenderer extends HumanoidMobRenderer<ToukenEntity, PlayerRenderState, PlayerModel> {
    /** モデル自体のデフォルトの身長 */
    protected final static float MODEL_BASE_HEIGHT = 2f;

    /** 刀剣男士の身長 */
    protected final float height;

    /** モデルのテクスチャの場所 */
    protected final ResourceLocation textureLocation;

    public ToukenEntityRenderer(EntityRendererProvider.Context context, ResourceLocation textureLocation, float height) {
        super(context, new PlayerModel(context.bakeLayer(ModelLayers.PLAYER), false), 0.5f);
        this.textureLocation = textureLocation;
        this.height = height;

        addLayer(new HumanoidArmorLayer<>(
                this,
                new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
                new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)),
                context.getEquipmentRenderer()
        ));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(PlayerRenderState renderState) {
        return textureLocation;
    }

    @Override
    public @NotNull PlayerRenderState createRenderState() {
        return new PlayerRenderState();
        // NOTE ここでスキンを設定すると、textureLocationがnullでクラッシュする constructorのsuperから呼ばれている？
    }

    @Override
    public void extractRenderState(ToukenEntity entity, PlayerRenderState renderState, float f) {
        super.extractRenderState(entity, renderState, f);
        renderState.skin = new PlayerSkin(textureLocation, null, null, null, PlayerSkin.Model.WIDE, true);
    }

    @Override
    protected void scale(PlayerRenderState renderState, PoseStack poseStack) {
        var scale = height / MODEL_BASE_HEIGHT;
        poseStack.scale(scale, scale, scale);
    }
}
