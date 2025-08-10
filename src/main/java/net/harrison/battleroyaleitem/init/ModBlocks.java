package net.harrison.battleroyaleitem.init;

import net.harrison.battleroyaleitem.Battleroyaleitem;
import net.harrison.battleroyaleitem.block.LootChestBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Battleroyaleitem.MODID);

    public static final RegistryObject<Block> LOOT_CHEST = registryBlock("loot_chest",
            () -> new LootChestBlock(BlockBehaviour.Properties.of().noOcclusion()));
    public static final RegistryObject<Block> COMMON_LOOT_CHEST = registryBlock("common_loot_chest",
            () -> new LootChestBlock(BlockBehaviour.Properties.of().noOcclusion()));
    public static final RegistryObject<Block> RARE_LOOT_CHEST = registryBlock("rare_loot_chest",
            () -> new LootChestBlock(BlockBehaviour.Properties.of().noOcclusion()));
    public static final RegistryObject<Block> EPIC_LOOT_CHEST = registryBlock("epic_loot_chest",
            () -> new LootChestBlock(BlockBehaviour.Properties.of().noOcclusion()));


    private static <T extends Block> RegistryObject<T> registryBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlocksItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlocksItem(String name, RegistryObject<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }


    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
