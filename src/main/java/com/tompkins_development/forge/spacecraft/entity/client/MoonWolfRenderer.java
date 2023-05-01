package com.tompkins_development.forge.spacecraft.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tompkins_development.forge.spacecraft.SpacecraftMod;
import com.tompkins_development.forge.spacecraft.entity.custom.MoonWolfEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MoonWolfRenderer extends GeoEntityRenderer<MoonWolfEntity> {

    public MoonWolfRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MoonWolfModel());
    }

    @Override
    public ResourceLocation getTextureLocation(MoonWolfEntity animatable) {
        return new ResourceLocation(SpacecraftMod.MOD_ID, "textures/entity/moon_wolf.png");
    }

    @Override
    public void render(MoonWolfEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        if(entity.isBaby()) {
            poseStack.scale(0.4f,0.4f,0.4f);
        }

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
