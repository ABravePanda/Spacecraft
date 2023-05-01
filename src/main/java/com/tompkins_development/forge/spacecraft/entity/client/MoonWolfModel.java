package com.tompkins_development.forge.spacecraft.entity.client;

import com.tompkins_development.forge.spacecraft.SpacecraftMod;
import com.tompkins_development.forge.spacecraft.entity.custom.MoonWolfEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.DataTicket;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class MoonWolfModel extends GeoModel<MoonWolfEntity> {

    @Override
    public ResourceLocation getModelResource(MoonWolfEntity animatable) {
        return new ResourceLocation(SpacecraftMod.MOD_ID, "geo/moon_wolf.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MoonWolfEntity animatable) {
        return new ResourceLocation(SpacecraftMod.MOD_ID, "textures/entity/moon_wolf.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MoonWolfEntity animatable) {
        return new ResourceLocation(SpacecraftMod.MOD_ID, "animations/moon_wolf.animation.json");
    }

    @Override
    public void setCustomAnimations(MoonWolfEntity animatable, long instanceId, AnimationState<MoonWolfEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("Head");

        if(head != null) {
            EntityModelData entityModelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityModelData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityModelData.netHeadYaw() * Mth.DEG_TO_RAD);
        } else
        {
            System.out.println("Head not found ----------------------");
        }
    }
}
