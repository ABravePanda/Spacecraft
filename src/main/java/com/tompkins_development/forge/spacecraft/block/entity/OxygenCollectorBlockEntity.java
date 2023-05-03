package com.tompkins_development.forge.spacecraft.block.entity;

import com.tompkins_development.forge.spacecraft.capabilities.IOxygenStorage;
import com.tompkins_development.forge.spacecraft.capabilities.ModCapabilities;
import com.tompkins_development.forge.spacecraft.item.custom.OxygenTankItem;
import com.tompkins_development.forge.spacecraft.networking.ModMessages;
import com.tompkins_development.forge.spacecraft.networking.packet.EnergySyncS2CPacket;
import com.tompkins_development.forge.spacecraft.networking.packet.OxygenSyncS2CPacket;
import com.tompkins_development.forge.spacecraft.screen.oxygen.OxygenCollectorMenu;
import com.tompkins_development.forge.spacecraft.util.ModEnergyStorage;
import com.tompkins_development.forge.spacecraft.util.ModOxygenStorage;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OxygenCollectorBlockEntity extends IOxygenBlockEntity implements MenuProvider {

    private final int OXYGEN_CREATION = 1;
    private final int OXYGEN_OUTPUT_RATE = 1;
    private static final int ENERGY_REQUIREMENT = 50;
    private final int ENERGY_CAPACITY = 50000;
    private final int ENERGY_TRANSFER_RATE = 200;

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 20;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private final ModEnergyStorage ENERGY_STORAGE = new ModEnergyStorage(ENERGY_CAPACITY, ENERGY_TRANSFER_RATE) {
        @Override
        public void onEnergyChanged() {
            setChanged();
            ModMessages.sendToClients(new EnergySyncS2CPacket(this.energy, getBlockPos()));
        }
    };


    public OxygenCollectorBlockEntity(BlockPos blockPos, BlockState state) {
        super(ModBlockEntities.OXYGEN_COLLECTOR.get(), blockPos, state);
        this.setOxygenCapacity(this,2000);
        this.setOxygenInputRate(this,200);
        this.setOxygenOutputRate(this,200);
        init();
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch(index) {
                    case 0 -> OxygenCollectorBlockEntity.this.progress;
                    case 1 -> OxygenCollectorBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch(index) {
                    case 0 -> OxygenCollectorBlockEntity.this.progress = value;
                    case 1 -> OxygenCollectorBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Oxygen Collector");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new OxygenCollectorMenu(id, inventory, this, this.data);
    }

    public IEnergyStorage getEnergyStorage() {
        return ENERGY_STORAGE;
    }


    public void setEnergyLevel(int energy) {
        this.ENERGY_STORAGE.setEnergy(energy);
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        }
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyEnergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyEnergyHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("oxygen_collector.progress", this.progress);
        tag.putInt("oxygen_collector.energy", ENERGY_STORAGE.getEnergyStored());
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        progress = tag.getInt("oxygen_collector.progress");
        ENERGY_STORAGE.setEnergy(tag.getInt("oxygen_collector.energy"));
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++)
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, OxygenCollectorBlockEntity entity) {
        IOxygenBlockEntity.tick(level, blockPos, blockState, entity);
        if(level.isClientSide) return;

        if(isAboveRedstoneBlock(entity, blockPos))
            entity.ENERGY_STORAGE.receiveEnergy(100, false);

        if(hasOxygenTankItem(entity)) {
            ItemStack item = entity.itemHandler.getStackInSlot(0);
            OxygenTankItem oxygenTank = (OxygenTankItem) item.getItem();
            if(!hasEnoughOxygen(entity)) return;
            if((oxygenTank.getOxygen(item) + entity.OXYGEN_OUTPUT_RATE) > oxygenTank.getCapacity(item)) return;
            extractOxygen(entity, entity.OXYGEN_OUTPUT_RATE);
            oxygenTank.addOxygen(item, entity.OXYGEN_OUTPUT_RATE);

        }

        if(level.dimension() == Level.OVERWORLD && hasEnoughEnergy(entity) && hasRoomForOxygen(entity)) {
            entity.progress++;
            extractEnergy(entity);
            setChanged(level, blockPos, blockState);
            
            if(entity.progress >= entity.maxProgress) {
                createOxygen(entity);
                setChanged(level, blockPos, blockState);
            }
        } else {
            entity.resetProgress();
            setChanged(level, blockPos, blockState);
        }
    }

    private static boolean hasEnoughOxygen(OxygenCollectorBlockEntity entity) {
        return entity.getOxygenStorage().getOxygenStored() >= entity.OXYGEN_OUTPUT_RATE;
    }


    private static void createOxygen(OxygenCollectorBlockEntity entity) {
        entity.getOxygenStorage().receiveOxygen(entity.OXYGEN_CREATION, false);
        entity.resetProgress();
    }

    private static boolean hasOxygenTankItem(OxygenCollectorBlockEntity entity) {
        return entity.itemHandler.getStackInSlot(0).getItem() instanceof OxygenTankItem;
    }


    private static boolean hasRoomForOxygen(OxygenCollectorBlockEntity entity) {
        return entity.getOxygenStorage().getOxygenStored() + entity.OXYGEN_CREATION <= entity.getOxygenStorage().getMaxOxygenStored();
    }


    private static void extractEnergy(OxygenCollectorBlockEntity entity) {
        entity.ENERGY_STORAGE.extractEnergy(ENERGY_REQUIREMENT, false);
    }

    private static void extractOxygen(OxygenCollectorBlockEntity entity, int amount) {
        entity.getOxygenStorage().extractOxygen(amount, false);
    }


    private static boolean hasEnoughEnergy(OxygenCollectorBlockEntity entity) {
        return entity.ENERGY_STORAGE.getEnergyStored() >= ENERGY_REQUIREMENT * entity.maxProgress;
    }

    private static boolean isAboveRedstoneBlock(OxygenCollectorBlockEntity entity, BlockPos blockPos) {
        return entity.level.getBlockState(blockPos.below()).getBlock() == Blocks.REDSTONE_BLOCK;
    }

    private void resetProgress() {
        this.progress = 0;
    }


    public int getOxygenCreationRate(OxygenCollectorBlockEntity oxygenCollectorBlockEntity) {
        return oxygenCollectorBlockEntity.OXYGEN_CREATION;
    }

}
