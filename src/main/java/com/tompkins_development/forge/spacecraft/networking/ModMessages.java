package com.tompkins_development.forge.spacecraft.networking;

import com.tompkins_development.forge.spacecraft.SpacecraftMod;
import com.tompkins_development.forge.spacecraft.networking.packet.EnergySyncS2CPacket;
import com.tompkins_development.forge.spacecraft.networking.packet.OxygenSyncS2CPacket;
import com.tompkins_development.forge.spacecraft.networking.packet.TankStateSyncC2SPacket;
import com.tompkins_development.forge.spacecraft.networking.packet.TankStateUpdateCableC2SPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(SpacecraftMod.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;


        net.messageBuilder(EnergySyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(EnergySyncS2CPacket::new)
                .encoder(EnergySyncS2CPacket::toBytes)
                .consumerMainThread(EnergySyncS2CPacket::handle)
                .add();

        net.messageBuilder(OxygenSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(OxygenSyncS2CPacket::new)
                .encoder(OxygenSyncS2CPacket::toBytes)
                .consumerMainThread(OxygenSyncS2CPacket::handle)
                .add();

        net.messageBuilder(TankStateSyncC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(TankStateSyncC2SPacket::new)
                .encoder(TankStateSyncC2SPacket::toBytes)
                .consumerMainThread(TankStateSyncC2SPacket::handle)
                .add();

        net.messageBuilder(TankStateUpdateCableC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(TankStateUpdateCableC2SPacket::new)
                .encoder(TankStateUpdateCableC2SPacket::toBytes)
                .consumerMainThread(TankStateUpdateCableC2SPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}