package com.tompkins_development.forge.spacecraft.screen.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tompkins_development.forge.spacecraft.capabilities.IOxygenStorage;
import com.tompkins_development.forge.spacecraft.conts.ConstColor;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;

import java.util.List;

public class OxygenInfoArea extends InfoArea {
    private final IOxygenStorage oxygen;
    private InfoAreaType type;

    public OxygenInfoArea(InfoAreaType type, int xMin, int yMin, IOxygenStorage oxygen, int width, int height)  {
        super(new Rect2i(xMin, yMin, width, height));
        this.oxygen = oxygen;
        this.type = type;
    }

    public List<Component> getTooltips() {
        return List.of(Component.literal(oxygen.getOxygenStored()+"/"+oxygen.getMaxOxygenStored()+" O2"));
    }

    @Override
    public void draw(PoseStack transform) {
        final int width = area.getWidth();
        final int height = area.getHeight();
        int stored = (int)(width*(oxygen.getOxygenStored()/(float)oxygen.getMaxOxygenStored()));
        switch (type) {
            case LEFT -> fillGradient(
                    transform,
                    area.getX(), area.getY(),
                    area.getX() + stored, area.getY() + area.getHeight(),
                    ConstColor.LIGHT_BLUE, ConstColor.DARK_BLUE
            );
            case RIGHT -> fillGradient(
                    transform,
                    area.getX() + (width - stored), area.getY(),
                    area.getX() + area.getWidth(), area.getY() + area.getHeight(),
                    ConstColor.LIGHT_BLUE, ConstColor.DARK_BLUE
            );
            case TOP -> fillGradient(
                    transform,
                    area.getX(), area.getY(),
                    area.getX() + area.getWidth(), area.getY() + (height - stored),
                    ConstColor.LIGHT_BLUE, ConstColor.DARK_BLUE
            );
            case BOTTOM -> fillGradient(
                    transform,
                    area.getX(), area.getY()+(height-stored),
                    area.getX() + area.getWidth(), area.getY() +area.getHeight(),
                    ConstColor.LIGHT_BLUE, ConstColor.DARK_BLUE
            );
        }

    }
}
