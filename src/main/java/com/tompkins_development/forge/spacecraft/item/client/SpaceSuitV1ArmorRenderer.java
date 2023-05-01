package com.tompkins_development.forge.spacecraft.item.client;

import com.tompkins_development.forge.spacecraft.item.custom.SpaceSuitV1ArmorItem;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class SpaceSuitV1ArmorRenderer extends GeoArmorRenderer<SpaceSuitV1ArmorItem> {

    public SpaceSuitV1ArmorRenderer() {
        super(new SpaceSuitV1ArmorModel());
    }

}
