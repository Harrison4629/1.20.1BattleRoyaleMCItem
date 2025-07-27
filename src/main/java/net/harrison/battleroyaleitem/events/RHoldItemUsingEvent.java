package net.harrison.battleroyaleitem.events;

import net.harrison.battleroyaleitem.Battleroyaleitem;
import net.harrison.battleroyaleitem.items.AbsRHoldItem;
import net.harrison.battleroyaleitem.util.ItemUsingSoundInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(modid = Battleroyaleitem.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class RHoldItemUsingEvent {
    private static final Map<UUID, ItemUsingSoundInstance> ACTIVE_SOUNDS = new ConcurrentHashMap<>();

    @SubscribeEvent
    public static void onItemUseStart(LivingEntityUseItemEvent.Start event) {
        if (event.getItem().getItem() instanceof AbsRHoldItem item) {
            playUsingSound(
                    event.getEntity(),
                    item.getUsingSound(),
                    item.getVolume(),
                    item.getPitch()
            );
        }
    }

    @SubscribeEvent
    public static void onItemUseStop(LivingEntityUseItemEvent.Stop event) {
        if (event.getEntity().level().isClientSide()) {
            stopUsingSound(event.getEntity());
        }
    }

    private static void playUsingSound(LivingEntity entity, SoundEvent soundEvent, float volume, float pitch) {
        if (entity == null || soundEvent == null) return;

        UUID entityId = entity.getUUID();

        ACTIVE_SOUNDS.compute(entityId, (uuid, existingSound) -> {
            if (existingSound != null) {
                existingSound.stopSound();
            }
            ItemUsingSoundInstance newSound = new ItemUsingSoundInstance(entity, soundEvent, volume, pitch);
            Minecraft.getInstance().getSoundManager().play(newSound);
            return newSound;
        });
    }

    private static void stopUsingSound(LivingEntity entity) {
        if (entity == null) return;
        UUID entityId = entity.getUUID();
        ACTIVE_SOUNDS.computeIfPresent(entityId, (uuid, sound) -> {
            sound.stopSound();
            return null;
        });
    }
}
