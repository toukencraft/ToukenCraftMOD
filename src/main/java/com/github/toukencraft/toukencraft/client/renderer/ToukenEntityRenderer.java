package com.github.toukencraft.toukencraft.client.renderer;

import com.github.toukencraft.toukencraft.entity.ToukenEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;


/** 刀剣男士のRenderer */
@Environment(EnvType.CLIENT)
public class ToukenEntityRenderer extends HumanoidMobRenderer<ToukenEntity, HumanoidRenderState, HumanoidModel<HumanoidRenderState>> {
    /** モデル自体のデフォルトの身長 */
    protected final static float MODEL_BASE_HEIGHT = 2f;

    /** 刀剣男士の身長 */
    protected final float height;

    /** モデルのテクスチャの場所 */
    protected final ResourceLocation textureLocation;

    public ToukenEntityRenderer(EntityRendererProvider.Context context, ResourceLocation textureLocation, float height) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
        this.textureLocation = textureLocation;
        this.height = height;

        addLayer(new HumanoidArmorLayer<>(
                this,
                ArmorModelSet.bake(
                        ModelLayers.PLAYER_ARMOR,
                        context.getModelSet(),
                        HumanoidModel::new
                ),
                context.getEquipmentRenderer()
        ));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(HumanoidRenderState renderState) {
        return textureLocation;
    }

    @Override
    public @NotNull HumanoidRenderState createRenderState() {
        return new HumanoidRenderState();
    }

    @Override
    public void extractRenderState(ToukenEntity entity, HumanoidRenderState renderState, float f) {
        super.extractRenderState(entity, renderState, f);
    }

    @Override
    protected void scale(HumanoidRenderState renderState, PoseStack poseStack) {
        var scale = height / MODEL_BASE_HEIGHT;
        poseStack.scale(scale, scale, scale);
    }
}
