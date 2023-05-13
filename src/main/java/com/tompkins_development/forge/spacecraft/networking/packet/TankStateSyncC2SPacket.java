package com.tompkins_development.forge.spacecraft.networking.packet;

import com.tompkins_development.forge.spacecraft.block.OxygenTankBlock;
import com.tompkins_development.forge.spacecraft.block.entity.IOxygenBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TankStateSyncC2SPacket {
    private BlockPos pos;
    private boolean state;
    private Direction direction;

    public TankStateSyncC2SPacket(Direction direction, boolean state, BlockPos pos) {
        this.state = state;
        this.pos = pos;
        this.direction = direction;
    }

    public TankStateSyncC2SPacket(FriendlyByteBuf buf) {
        this.state = buf.readBoolean();
        this.pos = buf.readBlockPos();
        this.direction = buf.readEnum(Direction.class);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(state);
        buf.writeBlockPos(pos);
        buf.writeEnum(direction);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerLevel level = context.getSender().getLevel();
            if(level.getBlockEntity(pos) instanceof IOxygenBlockEntity blockEntity) {
                if(state) {
                    blockEntity.getBlockState().setValue(OxygenTankBlock.inputDirection, direction);
                    level.setBlockAndUpdate(pos, blockEntity.getBlockState().setValue(OxygenTankBlock.inputDirection, direction));
                } else {
                    blockEntity.getBlockState().setValue(OxygenTankBlock.outputDirection, direction);
                    level.setBlockAndUpdate(pos, blockEntity.getBlockState().setValue(OxygenTankBlock.outputDirection, direction));
                }
            }
        });
        return true;
    }
}
