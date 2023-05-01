package com.tompkins_development.forge.spacecraft.datagen.block;

import com.tompkins_development.forge.spacecraft.SpacecraftMod;
import com.tompkins_development.forge.spacecraft.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {


    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, SpacecraftMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.TITANIUM_BLOCK);
        blockWithItem(ModBlocks.MOON_TITANIUM_ORE);
        blockWithItem(ModBlocks.MOON_GOLD_ORE);
        blockWithItem(ModBlocks.MOON_STONE);
        blockWithItem(ModBlocks.OXYGEN_TANK);
        horizontalBlock(ModBlocks.OXYGEN_COLLECTOR.get(), new ModelFile.UncheckedModelFile(modLoc("block/oxygen_collector")));
        horizontalBlock(ModBlocks.OXYGEN_CABLE.get(), new ModelFile.UncheckedModelFile(modLoc("block/oxygen_cable")));
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}