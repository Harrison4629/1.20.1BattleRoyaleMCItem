package net.harrison.battleroyaleitem.init;

import net.harrison.battleroyaleitem.Battleroyaleitem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS=
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Battleroyaleitem.MODID);

    public static final RegistryObject<SoundEvent> OPEN_LOOT_CHEST = registerSoundEvent("open_loot_chest");

    public static final RegistryObject<SoundEvent> CLOSE_LOOT_CHEST = registerSoundEvent("close_loot_chest");

    public static final RegistryObject<SoundEvent> ARMOR_PLATE_HIT = registerSoundEvent("armor_plate_hit");

    public static final RegistryObject<SoundEvent> ARMOR__PLATE_BREAK = registerSoundEvent("armor_plate_break");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Battleroyaleitem.MODID, name)));
    }

    public static void register(IEventBus bus) {
        SOUND_EVENTS.register(bus);
    }
}
