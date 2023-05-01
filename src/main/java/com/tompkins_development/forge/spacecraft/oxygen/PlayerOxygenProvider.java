package com.tompkins_development.forge.spacecraft.oxygen;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerOxygenProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<PlayerOxygen> PLAYER_OXYGEN = CapabilityManager.get(new CapabilityToken<PlayerOxygen>() {});

    private PlayerOxygen oxygen = null;
    private final LazyOptional<PlayerOxygen> optional = LazyOptional.of(this::createPlayerOxygen);

    private PlayerOxygen createPlayerOxygen() {
        if(this.oxygen == null)
            this.oxygen = new PlayerOxygen();
        return this.oxygen;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == PLAYER_OXYGEN)
            return optional.cast();
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerOxygen().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerOxygen().loadNBTData(nbt);
    }
}
