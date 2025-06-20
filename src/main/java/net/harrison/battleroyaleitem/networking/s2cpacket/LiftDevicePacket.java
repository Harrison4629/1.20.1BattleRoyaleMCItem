package net.harrison.battleroyaleitem.networking.s2cpacket;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class LiftDevicePacket {
    private final double speedX;
    private final double speedY;
    private final double speedZ;

    public LiftDevicePacket(Vec3 delta) {
        this.speedX = delta.x;
        this.speedY = delta.y;
        this.speedZ = delta.z;
    }

    public LiftDevicePacket(FriendlyByteBuf buf) {
        this.speedX = buf.readDouble();
        this.speedY = buf.readDouble();
        this.speedZ = buf.readDouble();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(speedX);
        buf.writeDouble(speedY);
        buf.writeDouble(speedZ);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                mc.player.playSound(SoundEvents.FIRECHARGE_USE, 0.5F, 1.0F);
                Vec3 delta = new Vec3(speedX, speedY, speedZ);
                mc.player.setDeltaMovement(delta);
            }
        });
        context.setPacketHandled(true);
    }
}
