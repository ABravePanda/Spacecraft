package com.tompkins_development.forge.spacecraft.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.energy.IEnergyStorage;

import static net.minecraftforge.common.capabilities.CapabilityManager.get;

public class ModCapabilities {
    public static final Capability<IOxygenStorage> OXYGEN = get(new CapabilityToken<>(){});
    public static final Capability<IOxygenStorage> OXYGEN_IN = get(new CapabilityToken<>(){});
    public static final Capability<IOxygenStorage> OXYGEN_OUT = get(new CapabilityToken<>(){});

}
