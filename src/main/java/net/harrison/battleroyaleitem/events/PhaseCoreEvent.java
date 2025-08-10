package net.harrison.battleroyaleitem.events;

import net.harrison.battleroyaleitem.Battleroyaleitem;
import net.harrison.battleroyaleitem.capabilities.phasecore.PhaseCoreProvider;
import net.harrison.battleroyaleitem.init.ModMessages;
import net.harrison.battleroyaleitem.networking.s2cpacket.DeltaMovementSyncS2CPacket;
import net.harrison.battleroyaleitem.util.ParticleSummon;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Battleroyaleitem.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PhaseCoreEvent {
    private static final float speed = 0.3F;

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.side.isClient()){
            Player player = event.player;
            player.getCapability(PhaseCoreProvider.PHASE_CORE_CAPABILITY).ifPresent(phaseCore -> {

                if (!phaseCore.isActive()) {
                    return;
                }

                if (phaseCore.isTimeUp()) {
                    ParticleSummon.spawnParticleSpiral(player.level(), player.getPosition(1.0F), 3, ParticleTypes.PORTAL);
                    player.teleportTo(phaseCore.getIniPos().x, phaseCore.getIniPos().y, phaseCore.getIniPos().z);
                    ParticleSummon.spawnParticleSpiral(player.level(), phaseCore.getIniPos(), 3, ParticleTypes.PORTAL);

                    player.displayClientMessage(Component.translatable("item.battleroyaleitem.phase_core.trace_back")
                            .withStyle(ChatFormatting.BLUE), true);
                    player.level().playSound(null, phaseCore.getIniPos().x, phaseCore.getIniPos().y, phaseCore.getIniPos().z,
                            SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 0.8F, 1.0F);

                    phaseCore.reset();

                } else {
                    phaseCore.subTicks();

                    player.setDeltaMovement(phaseCore.getDirection().scale(speed));

                    ModMessages.sendToPlayer(new DeltaMovementSyncS2CPacket(phaseCore.getDirection().scale(speed)), (ServerPlayer) player);
                    if (player.tickCount %5 == 0) {
                        ParticleSummon.teleportEffect(player.level(), player.getPosition(1.0F), 5);
                    }
                }
            });
        }
    }
}
