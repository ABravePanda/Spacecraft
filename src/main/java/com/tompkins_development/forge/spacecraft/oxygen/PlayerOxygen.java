package com.tompkins_development.forge.spacecraft.oxygen;

import net.minecraft.nbt.CompoundTag;

public class PlayerOxygen {

    private double oxygen;
    private final double MIN_OXYGEN = 0;
    private final double MAX_OXYGEN = 10;


    public double getOxygen() {
        return oxygen;
    }

    public void addOxygen(double oxygen) {
        this.oxygen = Math.min(this.oxygen + oxygen, MAX_OXYGEN);
    }

    public void subOxygen(double oxygen) {
        this.oxygen = Math.max(this.oxygen - oxygen, MIN_OXYGEN);
    }

    public void copyFrom(PlayerOxygen source) {
        this.oxygen = source.oxygen;
    }

    public void saveNBTData(CompoundTag tag) {
        tag.putDouble("oxygen", oxygen);
    }

    public void loadNBTData(CompoundTag tag) {
        oxygen = tag.getDouble("oxygen");
    }
}
