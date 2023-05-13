package com.tompkins_development.forge.spacecraft.networking.packet;

import com.tompkins_development.forge.spacecraft.block.ModBlocks;
import com.tompkins_development.forge.spacecraft.block.OxygenTankBlock;
import com.tompkins_development.forge.spacecraft.block.entity.IOxygenBlockEntity;
import com.tompkins_development.forge.spacecraft.block.entity.OxygenCableBlockEntity;
import com.tompkins_development.forge.spacecraft.util.Neighbor;
import com.tompkins_development.forge.spacecraft.util.PosUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class TankStateUpdateCableC2SPacket {
    private BlockPos pos;

    public TankStateUpdateCableC2SPacket(BlockPos pos) {
        this.pos = pos;
    }

    public TankStateUpdateCableC2SPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerLevel level = context.getSender().getLevel();
            if(level.getBlockEntity(pos) instanceof IOxygenBlockEntity blockEntity) {
                List<Neighbor> neighborList = PosUtil.getNeighboringBlocksByType(level, pos, ModBlocks.OXYGEN_CABLE.get());
                for(Neighbor neighbor : neighborList) {
                    if(level.getBlockEntity(neighbor.getPos()) instanceof OxygenCableBlockEntity oxygenCableBlockEntity) {
                        BlockPos masterCablePos = oxygenCableBlockEntity.getMaster(oxygenCableBlockEntity);
                        OxygenCableBlockEntity masterCable = (OxygenCableBlockEntity) level.getBlockEntity(masterCablePos);
                        if(masterCable == null) return;
                        masterCable.clearOutputs(masterCable);
                        masterCable.clearInputs(masterCable);
                    }
                }
            }
        });
        return true;
    }
}
