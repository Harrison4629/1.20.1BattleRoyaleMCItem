package net.harrison.battleroyaleitem.events;

import net.harrison.battleroyaleitem.Battleroyaleitem;
import net.harrison.battleroyaleitem.capabilities.temporary.PhaseData;
import net.harrison.battleroyaleitem.items.rholditem.PhaseCoreItem;
import net.harrison.battleroyaleitem.networking.c2spacket.StopPhasingPacket;
import net.harrison.battleroyaleitem.particles.ParticleSummon;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = Battleroyaleitem.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PhaseCoreEvent {
    private static final float speed = PhaseCoreItem.PHASE_SPEED;

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.side.isClient()) {
            if (!PhaseData.DATA.isEmpty()) {
                for (UUID playerId : PhaseData.DATA.keySet()) {
                    ServerPlayer player = event.getServer().getPlayerList().getPlayer(playerId);
                    if (player != null) {
                        updatePhasing(player);
                    }
                }
            }
        }
    }

    private static void updatePhasing(ServerPlayer player) {

        UUID playerId = player.getUUID();

        double dx = PhaseData.DATA.get(playerId).readDirection().x*speed;
        double dy = PhaseData.DATA.get(playerId).readDirection().y*speed;
        double dz = PhaseData.DATA.get(playerId).readDirection().z*speed;
        int timeRemaining = PhaseData.DATA.get(playerId).readRemainingTick();

        if (phaseFinished(playerId)) {

            ParticleSummon.spawnParticleSpiral(player.level(), player.getPosition(1.0F), 3, ParticleTypes.PORTAL);

            player.teleportTo(PhaseData.DATA.get(playerId).readOriginPos().x,
                    PhaseData.DATA.get(playerId).readOriginPos().y,
                    PhaseData.DATA.get(playerId).readOriginPos().z);

            PhaseData.DATA.remove(playerId);
            StopPhasingPacket.resetKeyPressed(playerId);

            player.displayClientMessage(Component.translatable("item.battleroyaleitem.phase_core.trace_back")
                    .withStyle(ChatFormatting.BLUE), true);
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 0.8F, 1.0F);
            ParticleSummon.spawnParticleCircle(player.level(), player.getPosition(1.0F), 1, ParticleTypes.PORTAL, 30);

        } else {
            if (timeRemaining %5 == 0) {
                ParticleSummon.teleportEffect(player.level(), player.getPosition(1.0F), 5);
            }
            player.teleportTo(player.getX() + dx, player.getY() + dy, player.getZ() + dz);

            PhaseData.DATA.get(playerId).modifyRemainingTick(timeRemaining-1);

        }

    }

    private static boolean phaseFinished(UUID playerId) {
        boolean finished = false;
        if (PhaseData.DATA.get(playerId).readRemainingTick() <=0) {
            finished = true;
        }

        if (StopPhasingPacket.isKeyPressed(playerId)) {
            finished = true;
        }

        return finished;
    }
}
