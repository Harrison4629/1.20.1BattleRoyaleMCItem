package net.harrison.battleroyaleitem.removal;

import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, String modid, ExistingFileHelper exFileHelper) {
        super(output, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

    }
    //public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
    //    super(output, Battleroyaleitem.MODID, exFileHelper);
    //}
//
    //@Override
    //protected void registerStatesAndModels() {
    //    simpleBlock(ModBlocks.LOOT_CHEST.get(), new ModelFile.UncheckedModelFile("block/loot_chest"));
    //}
//
    //private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
    //    simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    //}
}
