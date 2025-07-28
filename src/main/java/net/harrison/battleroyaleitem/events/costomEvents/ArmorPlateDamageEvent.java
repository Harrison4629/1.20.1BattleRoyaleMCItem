package net.harrison.battleroyaleitem.events.costomEvents;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Event;

public class ArmorPlateDamageEvent extends Event {

    private final LivingEntity entity;

    public ArmorPlateDamageEvent(LivingEntity entity) {
        this.entity = entity;
    }

    public LivingEntity getEntity(){
        return this.entity;
    }
}
