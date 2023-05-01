package com.tompkins_development.forge.spacecraft.networking.packet;

import com.tompkins_development.forge.spacecraft.block.entity.IOxygenBlockEntity;
import com.tompkins_development.forge.spacecraft.block.entity.OxygenCollectorBlockEntity;
import com.tompkins_development.forge.spacecraft.block.entity.OxygenTankBlockEntity;
import com.tompkins_development.forge.spacecraft.screen.oxygen.OxygenCollectorMenu;
import com.tompkins_development.forge.spacecraft.screen.oxygen.OxygenTankMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OxygenSyncS2CPacket {
    private final int oxygen;
    private final BlockPos pos;

    public OxygenSyncS2CPacket(int oxygen, BlockPos pos) {
        this.oxygen = oxygen;
        this.pos = pos;
    }

    public OxygenSyncS2CPacket(FriendlyByteBuf buf) {
        this.oxygen = buf.readInt();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(oxygen);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof OxygenCollectorBlockEntity blockEntity) {
                blockEntity.setOxygenLevel((IOxygenBlockEntity) Minecraft.getInstance().level.getBlockEntity(pos),oxygen);

                if(Minecraft.getInstance().player.containerMenu instanceof OxygenCollectorMenu menu && menu.getBlockEntity().getBlockPos().equals(pos)) {
                    blockEntity.setOxygenLevel((IOxygenBlockEntity) Minecraft.getInstance().level.getBlockEntity(pos),oxygen);
                }
            }

            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof OxygenTankBlockEntity blockEntity) {
                blockEntity.setOxygenLevel((IOxygenBlockEntity) Minecraft.getInstance().level.getBlockEntity(pos), oxygen);
                if(Minecraft.getInstance().player.containerMenu instanceof OxygenTankMenu menu && menu.getBlockEntity().getBlockPos().equals(pos)) {
                    blockEntity.setOxygenLevel((IOxygenBlockEntity) Minecraft.getInstance().level.getBlockEntity(pos),oxygen);
                }
            }
        });
        return true;
    }
}
