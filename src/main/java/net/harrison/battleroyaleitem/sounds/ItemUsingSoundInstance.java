package net.harrison.battleroyaleitem.sounds;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;

public class ItemUsingSoundInstance extends AbstractTickableSoundInstance {
    private final LivingEntity entity;
    private int ticksUsingItem;
    private boolean started = false;

    public ItemUsingSoundInstance(LivingEntity entity, SoundEvent soundEvent, float volume, float pitch) {
        super(soundEvent, SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.entity = entity;
        this.volume = volume;
        this.pitch = pitch;
        this.looping = true;
        this.delay = 0;
        this.relative = false;
        this.ticksUsingItem = 0;
    }

    public void stopSound() {
        this.stop();
    }

    @Override
    public void tick() {
        if (entity != null && entity.isAlive()) {
            if (entity.isUsingItem()) {
                // 更新声音位置为实体当前位置
                this.x = (float) entity.getX();
                this.y = (float) entity.getY();
                this.z = (float) entity.getZ();

                if (!started) {
                    started = true;
                }

                // 根据使用时间变化声音特性
                ticksUsingItem++;
                if (ticksUsingItem % 20 == 0) {
                    this.pitch = Math.max(0.8f, this.pitch - 0.01f);
                }
            } else {
                this.stop();
            }
        } else {
            this.stop();
        }

    }
}
