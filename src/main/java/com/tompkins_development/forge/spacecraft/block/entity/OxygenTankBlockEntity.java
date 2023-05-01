package com.tompkins_development.forge.spacecraft.block.entity;

import com.tompkins_development.forge.spacecraft.block.ModBlocks;
import com.tompkins_development.forge.spacecraft.capabilities.IOxygenStorage;
import com.tompkins_development.forge.spacecraft.capabilities.ModCapabilities;
import com.tompkins_development.forge.spacecraft.item.custom.OxygenTankItem;
import com.tompkins_development.forge.spacecraft.networking.ModMessages;
import com.tompkins_development.forge.spacecraft.networking.packet.EnergySyncS2CPacket;
import com.tompkins_development.forge.spacecraft.networking.packet.OxygenSyncS2CPacket;
import com.tompkins_development.forge.spacecraft.screen.oxygen.OxygenCollectorMenu;
import com.tompkins_development.forge.spacecraft.screen.oxygen.OxygenTankMenu;
import com.tompkins_development.forge.spacecraft.util.ModEnergyStorage;
import com.tompkins_development.forge.spacecraft.util.ModOxygenStorage;
import com.tompkins_development.forge.spacecraft.util.Neighbor;
import com.tompkins_development.forge.spacecraft.util.PosUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OxygenTankBlockEntity extends IOxygenBlockEntity implements MenuProvider {

    public OxygenTankBlockEntity(BlockPos blockPos, BlockState state) {
        super(ModBlockEntities.OXYGEN_TANK.get(), blockPos, state);
        this.setOxygenCapacity(this,10000);
        this.setOxygenInputRate(this,200);
        this.setOxygenOutputRate(this,200);
        init();
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Oxygen Tank");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new OxygenTankMenu(id, inventory, this, null);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
    }


    public static void tick(Level level, BlockPos blockPos, BlockState blockState, OxygenTankBlockEntity entity) {
        IOxygenBlockEntity.tick(level, blockPos, blockState, entity);
        if (level.isClientSide) return;

    }



}
