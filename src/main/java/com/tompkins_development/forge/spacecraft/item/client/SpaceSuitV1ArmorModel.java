package com.tompkins_development.forge.spacecraft.item.client;

import com.tompkins_development.forge.spacecraft.SpacecraftMod;
import com.tompkins_development.forge.spacecraft.item.custom.SpaceSuitV1ArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SpaceSuitV1ArmorModel extends GeoModel<SpaceSuitV1ArmorItem> {
    @Override
    public ResourceLocation getModelResource(SpaceSuitV1ArmorItem animatable) {
        return new ResourceLocation(SpacecraftMod.MOD_ID, "geo/spacesuit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SpaceSuitV1ArmorItem animatable) {
        return new ResourceLocation(SpacecraftMod.MOD_ID, "textures/armor/spacesuit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SpaceSuitV1ArmorItem animatable) {
        return new ResourceLocation(SpacecraftMod.MOD_ID, "animations/spacesuit.animation.json");
    }
}
