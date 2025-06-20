package net.harrison.battleroyaleitem.init;

import net.harrison.battleroyaleitem.Battleroyaleitem;
import net.harrison.battleroyaleitem.networking.c2spacket.StopPhasingPacket;
import net.harrison.battleroyaleitem.networking.s2cpacket.ArmorPlateSyncS2CPacket;
import net.harrison.battleroyaleitem.networking.s2cpacket.LiftDevicePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id () {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Battleroyaleitem.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        registerC2SPackets(net);

        registerS2CPackets(net);

    }

    private static void registerC2SPackets(SimpleChannel net) {
        net.messageBuilder(StopPhasingPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(StopPhasingPacket::new)
                .encoder(StopPhasingPacket::toBytes)
                .consumerMainThread(StopPhasingPacket::handle)
                .add();

    }

    private static void registerS2CPackets(SimpleChannel net) {
        net.messageBuilder(LiftDevicePacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(LiftDevicePacket::new)
                .encoder(LiftDevicePacket::toBytes)
                .consumerMainThread(LiftDevicePacket::handle)
                .add();

        net.messageBuilder(ArmorPlateSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ArmorPlateSyncS2CPacket::new)
                .encoder(ArmorPlateSyncS2CPacket::toBytes)
                .consumerMainThread(ArmorPlateSyncS2CPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
