package net.harrison.battleroyaleitem.init;

import net.harrison.battleroyaleitem.Battleroyaleitem;
import net.harrison.battleroyaleitem.items.placeitem.LiftDeviceItem;
import net.harrison.battleroyaleitem.items.rholditem.*;
import net.harrison.battleroyaleitem.items.rholditem.bioRadar.BioRadarItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Battleroyaleitem.MODID);

    public static final RegistryObject<Item> MEDKIT = ITEMS.register("medkit",
            () -> new MedkitItem(new Item.Properties().stacksTo(5)));
    public static final RegistryObject<Item> BANDAGE = ITEMS.register("bandage",
            () -> new BandageItem(new Item.Properties().stacksTo(8)));
    public static final RegistryObject<Item> CHAMELEON = ITEMS.register("chameleon",
            () -> new ChameleonItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> REGENERATION_SYRINGE = ITEMS.register("regeneration_syringe",
            () -> new RegenerationSyringeItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> PHASE_CORE = ITEMS.register("phase_core",
            () -> new PhaseCoreItem(new Item.Properties().stacksTo(3)));
    public static final RegistryObject<Item> BIO_RADAR = ITEMS.register("bio_radar",
            () -> new BioRadarItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> LIFT_DEVICE = ITEMS.register("lift_device",
            () -> new LiftDeviceItem(ModEntities.LIFTDEVICE, new Item.Properties().stacksTo(2)));
    public static final RegistryObject<Item> ARMOR_PLATE = ITEMS.register("armor_plate",
            () -> new ArmorPlateItem(new Item.Properties().stacksTo(3)));

}
