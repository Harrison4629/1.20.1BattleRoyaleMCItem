package net.harrison.battleroyaleitem.networking.c2spacket;

import net.harrison.battleroyaleitem.capabilities.phasecore.PhaseCore;
import net.harrison.battleroyaleitem.capabilities.phasecore.PhaseCoreProvider;
import net.harrison.battleroyaleitem.util.ParticleSummon;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class StopPhasingPacket {

    public StopPhasingPacket() {
    }

    public StopPhasingPacket(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }


    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                LazyOptional<PhaseCore> phaseCoreCapability = player.getCapability(PhaseCoreProvider.PHASE_CORE_CAPABILITY);
                phaseCoreCapability.ifPresent(phaseCore -> {
                    if (!phaseCore.isTimeUp()) {
                        double x = phaseCore.getIniPos().x;
                        double y = phaseCore.getIniPos().y;
                        double z = phaseCore.getIniPos().z;
                        phaseCore.reset();

                        ParticleSummon.spawnParticleSpiral(player.level(), player.getPosition(1.0F), 3, ParticleTypes.PORTAL);
                        player.teleportTo(x, y, z);
                        ParticleSummon.spawnParticleSpiral(player.level(), new Vec3(x, y, z), 3, ParticleTypes.PORTAL);

                        player.displayClientMessage(Component.translatable("item.battleroyaleitem.phase_core.trace_back")
                                .withStyle(ChatFormatting.BLUE), true);
                        player.level().playSound(null, x, y, z,
                                SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 0.8F, 1.0F);
                    }
                });
            }
        });
        context.setPacketHandled(true);
    }
}
