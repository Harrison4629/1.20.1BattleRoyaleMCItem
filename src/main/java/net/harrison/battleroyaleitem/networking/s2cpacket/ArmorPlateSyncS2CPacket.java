package net.harrison.battleroyaleitem.networking.s2cpacket;

import net.harrison.battleroyaleitem.client.ClientArmorPlateData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ArmorPlateSyncS2CPacket {
    private final int numOfArmorPlate;

    public ArmorPlateSyncS2CPacket(int numOfArmorPlate){
        this.numOfArmorPlate = numOfArmorPlate;
    }

    public ArmorPlateSyncS2CPacket(FriendlyByteBuf buf) {
        this.numOfArmorPlate = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(numOfArmorPlate);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientArmorPlateData.set(numOfArmorPlate);
        });
        context.setPacketHandled(true);
    }
}
