package com.tompkins_development.forge.spacecraft.screen.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tompkins_development.forge.spacecraft.conts.ConstColor;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.List;

public class EnergyInfoArea extends InfoArea {
    private final IEnergyStorage energy;

    public EnergyInfoArea(int xMin, int yMin)  {
        this(xMin, yMin, null,63,6);
    }

    public EnergyInfoArea(int xMin, int yMin, IEnergyStorage energy)  {
        this(xMin, yMin, energy,63,6);
    }

    public EnergyInfoArea(int xMin, int yMin, IEnergyStorage energy, int width, int height)  {
        super(new Rect2i(xMin, yMin, width, height));
        this.energy = energy;
    }

    public List<Component> getTooltips() {
        return List.of(Component.literal(energy.getEnergyStored()+"/"+energy.getMaxEnergyStored()+" FE"));
    }

    @Override
    public void draw(PoseStack transform) {
        final int width = area.getWidth();
        int stored = (int)(width*(energy.getEnergyStored()/(float)energy.getMaxEnergyStored()));
        fillGradient(
                transform,
                area.getX(), area.getY(),
                area.getX() + stored, area.getY() + area.getHeight(),
                ConstColor.LIGHT_RED, ConstColor.DARK_RED
        );
    }
}
