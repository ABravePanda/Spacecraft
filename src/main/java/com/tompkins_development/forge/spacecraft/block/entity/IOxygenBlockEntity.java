package com.tompkins_development.forge.spacecraft.block.entity;

import com.tompkins_development.forge.spacecraft.capabilities.IOxygenStorage;
import com.tompkins_development.forge.spacecraft.capabilities.ModCapabilities;
import com.tompkins_development.forge.spacecraft.networking.ModMessages;
import com.tompkins_development.forge.spacecraft.networking.packet.OxygenSyncS2CPacket;
import com.tompkins_development.forge.spacecraft.tags.ModTags;
import com.tompkins_development.forge.spacecraft.util.ModOxygenStorage;
import com.tompkins_development.forge.spacecraft.util.Neighbor;
import com.tompkins_development.forge.spacecraft.util.PosUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class IOxygenBlockEntity extends BlockEntity {

    private int oxygenCapacity;
    private int oxygenInputRate;
    private int oxygenOutputRate;

    private LazyOptional<IOxygenStorage> lazyOxygenHandler = LazyOptional.empty();

    private ModOxygenStorage OXYGEN_STORAGE;

    public IOxygenBlockEntity(BlockEntityType<?> blockEntity, BlockPos blockPos, BlockState state) {
        super(blockEntity, blockPos, state);
    }

    public void init() {
        OXYGEN_STORAGE = new ModOxygenStorage(oxygenCapacity, oxygenInputRate) {
            @Override
            public void onOxygenChanged() {
                setChanged();
                ModMessages.sendToClients(new OxygenSyncS2CPacket(this.oxygen, getBlockPos()));
            }
        };
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ModCapabilities.OXYGEN) {
            return lazyOxygenHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, IOxygenBlockEntity entity) {
        if (level.isClientSide) return;

        if(entity instanceof OxygenCableBlockEntity) {
            OxygenCableBlockEntity cableBlockEntity = (OxygenCableBlockEntity) entity;

            OxygenCableBlockEntity masterCable = (OxygenCableBlockEntity) level.getBlockEntity(cableBlockEntity.getMaster(cableBlockEntity));
            if(masterCable != null) {

                //Make Sure Input/Output Still Exist
                for (BlockPos output : new ArrayList<>(masterCable.getOutputs(masterCable)))
                    if(!(level.getBlockEntity(output) instanceof IOxygenBlockEntity))  {
                        masterCable.removeOutput(masterCable, output);
                        cableBlockEntity.removeOutput(cableBlockEntity, output);
                    }
                for (BlockPos input : new ArrayList<>(masterCable.getInputs(masterCable)))
                    if(!(level.getBlockEntity(input) instanceof IOxygenBlockEntity)) {
                        masterCable.removeInput(masterCable, input);
                        cableBlockEntity.removeInput(cableBlockEntity, input);
                    }


                //Double check all inputs and outputs
                List<Neighbor> neighbors = PosUtil.getNeighboringBlocksForOxygen(level, blockPos);
                for (Neighbor neighbor : neighbors) {
                    if (neighbor.getBlockState().is(ModTags.OXYGEN_INPUT)) {
                        if (!masterCable.getInputs(masterCable).contains(neighbor.getPos()))
                            masterCable.addInput(masterCable, neighbor.getPos());
                    }
                    if (neighbor.getBlockState().is(ModTags.OXYGEN_OUTPUT)) {
                        if (!masterCable.getOutputs(masterCable).contains(neighbor.getPos()))
                            masterCable.addOutput(masterCable, neighbor.getPos());
                    }
                }

                //Handle All Outputs (Things putting oxygen into cables)
                for (BlockPos output : masterCable.getOutputs(masterCable)) {
                    IOxygenBlockEntity outputEntity = (IOxygenBlockEntity) level.getBlockEntity(output);
                    masterCable.getCapability(ModCapabilities.OXYGEN).ifPresent((cableOxygen) -> {
                        outputEntity.getCapability(ModCapabilities.OXYGEN).ifPresent((blockOxygen) -> {
                           if(cableHasSpace(cableOxygen, masterCable.getOxygenInputRate(masterCable)) && blockHasEnoughOxygen(blockOxygen)) {
                               int oxygen = getOxygenToChangeOutput(masterCable, cableOxygen, blockOxygen);
                               blockOxygen.extractOxygen(oxygen, false);
                               cableOxygen.receiveOxygen(oxygen, false);
                               setChanged(level, blockPos, blockState);
                           }
                        });
                    });
                }

                //Handle All Inputs (Things pulling oxygen from cables)
                for (BlockPos input : masterCable.getInputs(masterCable)) {
                    IOxygenBlockEntity inputEntity = (IOxygenBlockEntity) level.getBlockEntity(input);
                    masterCable.getCapability(ModCapabilities.OXYGEN).ifPresent((cableOxygen) -> {
                        inputEntity.getCapability(ModCapabilities.OXYGEN).ifPresent((blockOxygen) -> {
                            if(objectHasSpace(blockOxygen, masterCable) && cableHasOxygen(cableOxygen)) {
                                int oxygen = getOxygenToChangeInput(masterCable, cableOxygen, blockOxygen);
                                cableOxygen.extractOxygen(oxygen, false);
                                blockOxygen.receiveOxygen(oxygen, false);
                                setChanged(level, blockPos, blockState);
                            }
                        });
                    });
                }
            }
            else System.out.println("Master is null @ " + blockPos);
        }
    }

    private static boolean cableHasOxygen(IOxygenStorage cableOxygen) {
        return cableOxygen.getOxygenStored() >= 0;
    }

    private static boolean objectHasSpace(IOxygenStorage blockOxygen, OxygenCableBlockEntity masterCable) {
        int amt = Math.min(masterCable.getOxygenOutputRate(masterCable), blockOxygen.getMaxOxygenStored()-blockOxygen.getOxygenStored());
        return amt > 0;
    }

    private static int getOxygenToChangeOutput(OxygenCableBlockEntity masterCable, IOxygenStorage cableOxygen, IOxygenStorage blockOxygen) {
        int spaceLeftInCable = cableOxygen.getMaxOxygenStored()-cableOxygen.getOxygenStored();
        int oxygenInBlock = blockOxygen.getOxygenStored();
        int firstNum = Math.min(spaceLeftInCable, oxygenInBlock);
        return Math.min(firstNum, masterCable.getOxygenInputRate(masterCable));
    }

    private static int getOxygenToChangeInput(OxygenCableBlockEntity masterCable, IOxygenStorage cableOxygen, IOxygenStorage blockOxygen) {
        int spaceLeftInBuilding = blockOxygen.getMaxOxygenStored()-blockOxygen.getOxygenStored();
        int oxygenLeftInCable = cableOxygen.getOxygenStored();
        int firstNum = Math.min(spaceLeftInBuilding, oxygenLeftInCable);
        return Math.min(firstNum, masterCable.getOxygenOutputRate(masterCable));
    }


    private static boolean blockHasEnoughOxygen(IOxygenStorage blockOxygen) {
        return blockOxygen.getOxygenStored() >= 0;
    }

    private static boolean cableHasSpace(IOxygenStorage cableOxygen, int rate) {
        //Most amt of oxygen can never be greater than the cable transfer rate.
        int amt = Math.min(rate, cableOxygen.getMaxOxygenStored()-cableOxygen.getOxygenStored());
        return amt > 0;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyOxygenHandler = LazyOptional.of(() -> OXYGEN_STORAGE);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyOxygenHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.putInt("oxygen_stored", OXYGEN_STORAGE.getOxygenStored());
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);;
        OXYGEN_STORAGE.setOxygen(tag.getInt("oxygen_stored"));
    }


    public IOxygenStorage getOxygenStorage() {
        return OXYGEN_STORAGE;
    }

    public void setOxygenLevel(IOxygenBlockEntity entity, int oxygenLevel) {
        entity.OXYGEN_STORAGE.setOxygen(oxygenLevel);
    }

    public int getOxygenCapacity(IOxygenBlockEntity entity) {
        return entity.oxygenCapacity;
    }

    public void setOxygenCapacity(IOxygenBlockEntity entity, int oxygenCapacity) {
        entity.oxygenCapacity = oxygenCapacity;
    }

    public int getOxygenInputRate(IOxygenBlockEntity entity) {
        return entity.oxygenInputRate;
    }

    public void setOxygenInputRate(IOxygenBlockEntity entity, int oxygenInputRate) {
        entity.oxygenInputRate = oxygenInputRate;
    }

    public int getOxygenOutputRate(IOxygenBlockEntity entity ) {
        return entity.oxygenOutputRate;
    }

    public void setOxygenOutputRate(IOxygenBlockEntity entity, int oxygenOutputRate) {
        entity.oxygenOutputRate = oxygenOutputRate;
    }
}
