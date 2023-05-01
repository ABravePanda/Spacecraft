package com.tompkins_development.forge.spacecraft.util;

import com.tompkins_development.forge.spacecraft.capabilities.OxygenStorage;

public abstract class ModOxygenStorage extends OxygenStorage {

    public ModOxygenStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    @Override
    public int extractOxygen(int maxExtract, boolean simulate) {
        int oxygenExtracted = Math.min(oxygen, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
            oxygen -= oxygenExtracted;
        if(oxygenExtracted != 0)
            onOxygenChanged();
        return oxygenExtracted;
    }

    @Override
    public int receiveOxygen(int maxReceive, boolean simulate) {
        int oxygenReceived = Math.min(capacity - oxygen, Math.min(this.maxReceive, maxReceive));
        if (!simulate)
            oxygen += oxygenReceived;
        if(oxygenReceived != 0)
            onOxygenChanged();
        return oxygenReceived;
    }

    public int setOxygen(int oxygen) {
        this.oxygen = oxygen;
        return oxygen;
    }

    public abstract void onOxygenChanged();
}
