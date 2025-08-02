package net.harrison.battleroyaleitem.removal;

import net.harrison.battleroyaleitem.Battleroyaleitem;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Battleroyaleitem.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    //@SubscribeEvent
    //public static void gatherData(GatherDataEvent event) {
    //    DataGenerator generator = event.getGenerator();
    //    PackOutput packOutput = generator.getPackOutput();
    //    ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
//
    //    generator.addProvider(event.includeServer(), new ModBlockStateProvider(packOutput, existingFileHelper));
//
    //}
}
