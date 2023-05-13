package com.tompkins_development.forge.spacecraft.block.entity;

import com.tompkins_development.forge.spacecraft.block.OxygenTankBlock;
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

public class IOxygenBlockEntity extends BlockEntity  {

    private int oxygenCapacity;
    private int oxygenInputRate;
    private int oxygenOutputRate;
    private int oxygenCapacityRemaining;

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
                for (BlockPos output : new ArrayList<>(masterCable.getOutputs(masterCable))) {
                    if (!(level.getBlockEntity(output) instanceof IOxygenBlockEntity)) {
                        masterCable.removeOutput(masterCable, output);
                        cableBlockEntity.removeOutput(cableBlockEntity, output);
                    }
                }
                for (BlockPos input : new ArrayList<>(masterCable.getInputs(masterCable))) {
                    if (!(level.getBlockEntity(input) instanceof IOxygenBlockEntity)) {
                        masterCable.removeInput(masterCable, input);
                        cableBlockEntity.removeInput(cableBlockEntity, input);
                    }
                }


                //Double check all inputs and outputs
                List<Neighbor> neighbors = PosUtil.getNeighboringBlocksForOxygen(level, blockPos);
                for (Neighbor neighbor : neighbors) {
                    if (neighbor.getBlockState().is(ModTags.OXYGEN_INPUT)) {
                        if (!masterCable.getInputs(masterCable).contains(neighbor.getPos())) {
                            IOxygenBlockEntity blockEntity = (IOxygenBlockEntity) level.getBlockEntity(neighbor.getPos());
                            blockEntity.getCapability(ModCapabilities.OXYGEN, neighbor.getDirection()).ifPresent((a) -> {
                                if(blockEntity instanceof OxygenTankBlockEntity) {
                                    if(neighbor.getDirection().getOpposite() == blockEntity.getBlockState().getValue(OxygenTankBlock.inputDirection))
                                        masterCable.addInput(masterCable, neighbor.getPos());
                                } else
                                    masterCable.addInput(masterCable, neighbor.getPos());
                            });

                        }
                    }
                    if (neighbor.getBlockState().is(ModTags.OXYGEN_OUTPUT)) {
                        if (!masterCable.getOutputs(masterCable).contains(neighbor.getPos())) {
                            IOxygenBlockEntity blockEntity = (IOxygenBlockEntity) level.getBlockEntity(neighbor.getPos());
                            blockEntity.getCapability(ModCapabilities.OXYGEN, neighbor.getDirection()).ifPresent((a) -> {
                                if(blockEntity instanceof OxygenTankBlockEntity) {
                                    if(neighbor.getDirection().getOpposite() == blockEntity.getBlockState().getValue(OxygenTankBlock.outputDirection))
                                        masterCable.addOutput(masterCable, neighbor.getPos());
                                } else
                                    masterCable.addOutput(masterCable, neighbor.getPos());
                            });
                        }
                    }
                }

                for(BlockPos outputPos : masterCable.getOutputs(masterCable)) {
                    if(masterCable.getInputs(masterCable).size() == 0) continue;
                    int leftover = 0;
                    final int[] outputOxygenPresent = new int[1];
                    IOxygenBlockEntity outputEntity = (IOxygenBlockEntity) level.getBlockEntity(outputPos);

                    outputEntity.getCapability(ModCapabilities.OXYGEN).ifPresent((outputOxygen) -> {
                       outputOxygenPresent[0] = outputOxygen.getOxygenStored();
                    });

                    int amountToOutput = (Math.min(outputEntity.getOxygenOutputRate(outputEntity) + leftover, outputOxygenPresent[0] + leftover)) / masterCable.getInputs(masterCable).size();
                    for(BlockPos inputPos : masterCable.getInputs(masterCable)) {
                        IOxygenBlockEntity inputEntity = (IOxygenBlockEntity) level.getBlockEntity(inputPos);
                        int amountToPutIn = Math.min(amountToOutput,Math.min(inputEntity.getOxygenCapacityRemaining(inputEntity), inputEntity.getOxygenInputRate(inputEntity)));
                        leftover = amountToOutput - amountToPutIn;

                        outputEntity.getCapability(ModCapabilities.OXYGEN).ifPresent((outputOxygen) -> {
                            inputEntity.getCapability(ModCapabilities.OXYGEN).ifPresent((inputOxygen) -> {
                                if(outputEntity.getBlockPos() != inputEntity.getBlockPos()) {
                                    outputOxygen.extractOxygen(amountToPutIn, false);
                                    inputOxygen.receiveOxygen(amountToPutIn, false);
                                }
                            });
                        });
                    }
                }

            }
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

    public int getOxygenCapacityRemaining(IOxygenBlockEntity entity) {
        return entity.getOxygenStorage().getMaxOxygenStored()-entity.getOxygenStorage().getOxygenStored();
    }

    public LazyOptional<IOxygenStorage> getLazyOxygenHandler() {
        return lazyOxygenHandler;
    }
}
