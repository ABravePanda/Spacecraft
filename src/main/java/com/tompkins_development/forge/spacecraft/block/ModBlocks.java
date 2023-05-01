package com.tompkins_development.forge.spacecraft.block;

import com.tompkins_development.forge.spacecraft.SpacecraftMod;
import com.tompkins_development.forge.spacecraft.item.ModItems;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, SpacecraftMod.MOD_ID);

    public static final RegistryObject<Block> TITANIUM_BLOCK = registerBlock("titanium_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(6f).requiresCorrectToolForDrops()));

    /* MOON */

    public static final RegistryObject<Block> MOON_STONE = registerBlock("moon_stone",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(3f, 3f).requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> MOON_GOLD_ORE = registerBlock("moon_gold_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(3f, 3f).requiresCorrectToolForDrops(), UniformInt.of(2, 6)));

    public static final RegistryObject<Block> MOON_TITANIUM_ORE = registerBlock("moon_titanium_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(3f, 3f).requiresCorrectToolForDrops(), UniformInt.of(2, 6)));

    /* Oxygen */

    public static final RegistryObject<Block> OXYGEN_CABLE = registerBlock("oxygen_cable",
            ()-> new OxygenCableBlock());

    public static final RegistryObject<Block> OXYGEN_COLLECTOR = registerBlock("oxygen_collector",
            ()-> new OxygenCollectorBlock());

    public static final RegistryObject<Block> OXYGEN_TANK = registerBlock("oxygen_tank",
            ()-> new OxygenTankBlock());

//    public static final RegistryObject<Block> DEEPSLATE_BLACK_OPAL_ORE = registerBlock("deepslate_black_opal_ore",
//            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
//                    .strength(8f).requiresCorrectToolForDrops(), UniformInt.of(2, 6)));
//    public static final RegistryObject<Block> NETHERRACK_BLACK_OPAL_ORE = registerBlock("netherrack_black_opal_ore",
//            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
//                    .strength(4f).requiresCorrectToolForDrops(), UniformInt.of(2, 6)));
//    public static final RegistryObject<Block> ENDSTONE_BLACK_OPAL_ORE = registerBlock("endstone_black_opal_ore",
//            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
//                    .strength(9f).requiresCorrectToolForDrops(), UniformInt.of(2, 6)));



    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}