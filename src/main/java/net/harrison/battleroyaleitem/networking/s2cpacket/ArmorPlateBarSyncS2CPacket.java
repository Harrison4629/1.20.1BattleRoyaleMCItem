package net.harrison.battleroyaleitem.networking.s2cpacket;

import net.harrison.battleroyaleitem.data.ClientArmorPlateData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ArmorPlateBarSyncS2CPacket {
    private final int numOfArmorPlate;
    private final float armorPlateHP;

    public ArmorPlateBarSyncS2CPacket(int numOfArmorPlate, float armorPlateHP){
        this.numOfArmorPlate = numOfArmorPlate;
        this.armorPlateHP = armorPlateHP;
    }

    public ArmorPlateBarSyncS2CPacket(FriendlyByteBuf buf) {
        this.numOfArmorPlate = buf.readInt();
        this.armorPlateHP = buf.readFloat();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(numOfArmorPlate);
        buf.writeFloat(armorPlateHP);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientArmorPlateData.set(numOfArmorPlate, armorPlateHP);
        });
        context.setPacketHandled(true);
    }
}
