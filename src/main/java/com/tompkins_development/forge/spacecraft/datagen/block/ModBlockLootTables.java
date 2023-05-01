package com.tompkins_development.forge.spacecraft.datagen.block;

import com.tompkins_development.forge.spacecraft.block.ModBlocks;
import com.tompkins_development.forge.spacecraft.item.ModItems;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.TITANIUM_BLOCK.get());
        dropSelf(ModBlocks.MOON_STONE.get());
        dropSelf(ModBlocks.OXYGEN_COLLECTOR.get());
        dropSelf(ModBlocks.OXYGEN_CABLE.get());
        dropSelf(ModBlocks.OXYGEN_TANK.get());

        add(ModBlocks.MOON_TITANIUM_ORE.get(),
                (block) -> createOreDrop(ModBlocks.MOON_TITANIUM_ORE.get(), ModItems.RAW_TITANIUM.get()));
        add(ModBlocks.MOON_GOLD_ORE.get(),
                (block) -> createOreDrop(ModBlocks.MOON_TITANIUM_ORE.get(), Items.RAW_GOLD));
//        add(ModBlocks.DEEPSLATE_BLACK_OPAL_ORE.get(),
//                (block) -> createOreDrop(ModBlocks.DEEPSLATE_BLACK_OPAL_ORE.get(), ModItems.RAW_BLACK_OPAL.get()));
//        add(ModBlocks.NETHERRACK_BLACK_OPAL_ORE.get(),
//                (block) -> createOreDrop(ModBlocks.NETHERRACK_BLACK_OPAL_ORE.get(), ModItems.RAW_BLACK_OPAL.get()));
//        add(ModBlocks.ENDSTONE_BLACK_OPAL_ORE.get(),
//                (block) -> createOreDrop(ModBlocks.ENDSTONE_BLACK_OPAL_ORE.get(), ModItems.RAW_BLACK_OPAL.get()));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}

