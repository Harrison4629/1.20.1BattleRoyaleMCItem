package net.harrison.battleroyaleitem.init;

import net.harrison.battleroyaleitem.Battleroyaleitem;
import net.harrison.battleroyaleitem.capabilities.armorplate.ArmorPlate;
import net.harrison.battleroyaleitem.capabilities.armorplate.ArmorPlateProvider;
import net.harrison.battleroyaleitem.capabilities.phasecore.PhaseCore;
import net.harrison.battleroyaleitem.capabilities.phasecore.PhaseCoreProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = Battleroyaleitem.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayers(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(ArmorPlateProvider.ARMOR_PLATE_CAPABILITY).isPresent()) {
                event.addCapability(ResourceLocation.fromNamespaceAndPath(Battleroyaleitem.MODID, "armor_plate"), new ArmorPlateProvider());
            }
            if (!event.getObject().getCapability(PhaseCoreProvider.PHASE_CORE_CAPABILITY).isPresent()) {
                event.addCapability(ResourceLocation.fromNamespaceAndPath(Battleroyaleitem.MODID, "phase_core"), new PhaseCoreProvider());
            }
        }
    }

    //@SubscribeEvent
    //public static void onPlayerCloned(PlayerEvent.Clone event) {
    //    if (event.isWasDeath()) {
    //        event.getOriginal().getCapability(ArmorPlateProvider.NUMOF_ARMOR_PLATE_CAPABILITY).ifPresent(oldStore -> {
    //            event.getOriginal().getCapability(ArmorPlateProvider.NUMOF_ARMOR_PLATE_CAPABILITY).ifPresent(newStore -> {
    //                newStore.copyFrom(oldStore);
    //            });
    //        });
    //        event.getOriginal().getCapability(PhaseCoreProvider.PHASE_CORE_CAPABILITY).ifPresent(oldStore -> {
    //            event.getOriginal().getCapability(PhaseCoreProvider.PHASE_CORE_CAPABILITY).ifPresent(newStore -> {
    //                newStore.copyFrom(oldStore);
    //            });
    //        });
    //    }
    //}

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(ArmorPlate.class);
        event.register(PhaseCore.class);
    }
}
