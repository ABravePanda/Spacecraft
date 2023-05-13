package com.tompkins_development.forge.spacecraft.block.entity;

import com.tompkins_development.forge.spacecraft.block.ModBlocks;
import com.tompkins_development.forge.spacecraft.capabilities.IOxygenStorage;
import com.tompkins_development.forge.spacecraft.capabilities.ModCapabilities;
import com.tompkins_development.forge.spacecraft.networking.ModMessages;
import com.tompkins_development.forge.spacecraft.networking.packet.OxygenSyncS2CPacket;
import com.tompkins_development.forge.spacecraft.util.ModOxygenStorage;
import com.tompkins_development.forge.spacecraft.util.Neighbor;
import com.tompkins_development.forge.spacecraft.util.PosUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class OxygenCableBlockEntity extends IOxygenBlockEntity {

    private BlockPos master;
    private List<BlockPos> inputs,outputs;
    

    public OxygenCableBlockEntity(BlockPos blockPos, BlockState state) {
        super(ModBlockEntities.OXYGEN_CABLE.get(), blockPos, state);
        this.setOxygenCapacity(this,0);
        this.setOxygenInputRate(this,100);
        this.setOxygenOutputRate(this,100);
        init();
        master = new BlockPos(-10000,-10000,-10000);
        if(inputs == null)
            this.inputs = new ArrayList<>();
        if(outputs == null)
            this.outputs = new ArrayList<>();
    }


    public static void tick(Level level, BlockPos blockPos, BlockState blockState, OxygenCableBlockEntity entity) {
        IOxygenBlockEntity.tick(level, blockPos, blockState, entity);
        if (level.isClientSide) return;
    }
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("oxygen_pipe.master", NbtUtils.writeBlockPos(master));
        ListTag listTag = new ListTag();
        for(BlockPos pos : inputs)
            listTag.add(NbtUtils.writeBlockPos(pos));
        tag.put("oxygen_pipe.inputs", listTag);
        listTag.clear();
        for(BlockPos pos : outputs)
            listTag.add(NbtUtils.writeBlockPos(pos));
        tag.put("oxygen_pipe.outputs", listTag);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        master = NbtUtils.readBlockPos(tag.getCompound("oxygen_pipe.master"));
        inputs.clear();
        outputs.clear();
        ListTag listTag = tag.getList("oxygen_pipe.inputs", Tag.TAG_COMPOUND);
        for(int i = 0; i < listTag.size(); i++) {
            inputs.add(NbtUtils.readBlockPos(listTag.getCompound(i)));
        }
        listTag.clear();
        listTag = tag.getList("oxygen_pipe.outputs", Tag.TAG_COMPOUND);
        for(int i = 0; i < listTag.size(); i++) {
            outputs.add(NbtUtils.readBlockPos(listTag.getCompound(i)));
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }


    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
    }

    public void addInput(OxygenCableBlockEntity entity, BlockPos pos) {
        entity.inputs.add(pos);
    }

    public List<BlockPos> getInputs (OxygenCableBlockEntity entity) {
        return entity.inputs;
    }

    public void addOutput(OxygenCableBlockEntity entity, BlockPos pos) {
        entity.outputs.add(pos);
    }

    public void removeOutput(OxygenCableBlockEntity entity, BlockPos pos) {
        if(entity.outputs.contains(pos))
            entity.outputs.remove(pos);
    }

    public void removeInput(OxygenCableBlockEntity entity, BlockPos pos) {
        if(entity.inputs.contains(pos))
            entity.inputs.remove(pos);
    }

    public void clearInputs(OxygenCableBlockEntity entity) {
        entity.inputs.clear();
    }

    public void clearOutputs(OxygenCableBlockEntity entity) {
        entity.outputs.clear();
    }

    public List<BlockPos> getOutputs(OxygenCableBlockEntity entity) {
        return entity.outputs;
    }

    public BlockPos getMaster(OxygenCableBlockEntity entity) {
        return entity.master;
    }

    public void setMaster(OxygenCableBlockEntity entity, BlockPos master) {
        entity.master = master;
    }
    
}
