package net.harrison.battleroyaleitem.init;

import net.harrison.battleroyaleitem.Battleroyaleitem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Battleroyaleitem.MODID);

    public static final RegistryObject<CreativeModeTab> BATTLEROYALE_ITEM_TAB = CREATIVE_MODE_TABS.register("battleroyaleitemtab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.battleroyaleitemtab"))
                    .icon(() -> new ItemStack(ModItems.MEDKIT.get()))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.MEDKIT.get());
                        output.accept(ModItems.BANDAGE.get());
                        output.accept(ModItems.CHAMELEON.get());
                        output.accept(ModItems.REGENERATION_SYRINGE.get());
                        output.accept(ModItems.PHASE_CORE.get());
                        output.accept(ModItems.BIO_RADAR.get());
                        output.accept(ModItems.LIFT_DEVICE.get());
                        output.accept(ModItems.ARMOR_PLATE.get());
                        output.accept(ModBlocks.LOOT_CHEST.get());
                        output.accept(ModBlocks.COMMON_LOOT_CHEST.get());
                        output.accept(ModBlocks.RARE_LOOT_CHEST.get());
                        output.accept(ModBlocks.EPIC_LOOT_CHEST.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
