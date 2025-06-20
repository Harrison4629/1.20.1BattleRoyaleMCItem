package net.harrison.battleroyaleitem.events;

import net.harrison.battleroyaleitem.Battleroyaleitem;
import net.harrison.battleroyaleitem.items.AbsRHoldItem;
import net.harrison.battleroyaleitem.sounds.ItemUsingSoundInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Battleroyaleitem.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class RHoldItemUsingEvent {
    private static final Map<UUID, ItemUsingSoundInstance> ACTIVE_SOUNDS = new HashMap<>();

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

    private static void playUsingSound(LivingEntity entity, SoundEvent soundEvent, float volume, float pitch) {
        if (entity == null || soundEvent == null) return;

        UUID entityId = entity.getUUID();
        // 停止该实体的任何现有声音
        stopUsingSound(entity);

        // 创建新的声音实例并开始播放
        ItemUsingSoundInstance soundInstance = new ItemUsingSoundInstance(entity, soundEvent, volume, pitch);
        ACTIVE_SOUNDS.put(entityId, soundInstance);
        Minecraft.getInstance().getSoundManager().play(soundInstance);
    }

    private static void stopUsingSound(LivingEntity entity) {
        if (entity == null) return;
        UUID entityId = entity.getUUID();
        if (ACTIVE_SOUNDS.containsKey(entityId)) {
            ItemUsingSoundInstance sound = ACTIVE_SOUNDS.get(entityId);
            sound.stopSound();
            ACTIVE_SOUNDS.remove(entityId);
        }
    }
}
