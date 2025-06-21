package net.harrison.battleroyaleitem.events;

import net.harrison.battleroyaleitem.Battleroyaleitem;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Battleroyaleitem.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LiftDeviceFallImmuneEvent {
    private static final Map<UUID, Boolean> LIFE_DEVICE_FALL_IMMUNE = new HashMap<>();

    private static boolean isImmune(UUID playerId) {
        return LIFE_DEVICE_FALL_IMMUNE.getOrDefault(playerId, false);
    }

    public static void setImmune(UUID playerId) {
        LIFE_DEVICE_FALL_IMMUNE.put(playerId, true);
    }

    public static void resetImmune(UUID playerId) {
        LIFE_DEVICE_FALL_IMMUNE.remove(playerId);
    }

    @SubscribeEvent
    public static void onPlayerFall(LivingFallEvent event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (isImmune(player.getUUID())) {
            event.setCanceled(true);
            resetImmune(player.getUUID());
        }
    }
}
