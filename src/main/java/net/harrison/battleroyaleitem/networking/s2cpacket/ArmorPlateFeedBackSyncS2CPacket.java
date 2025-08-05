package net.harrison.battleroyaleitem.networking.s2cpacket;

import net.harrison.battleroyaleitem.screens.ArmorPlateDamageFeedbackHudOverlay;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ArmorPlateFeedBackSyncS2CPacket {
    private final boolean isBroke;

    public ArmorPlateFeedBackSyncS2CPacket(boolean isBroke){
        this.isBroke = isBroke;
    }

    public ArmorPlateFeedBackSyncS2CPacket(FriendlyByteBuf buf) {
        this.isBroke = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(isBroke);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {

            ArmorPlateDamageFeedbackHudOverlay.setDisplay(this.isBroke);

        });
        context.setPacketHandled(true);
    }
}
