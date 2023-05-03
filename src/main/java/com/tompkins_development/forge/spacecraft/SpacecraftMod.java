package com.tompkins_development.forge.spacecraft;

import com.tompkins_development.forge.spacecraft.block.ModBlocks;
import com.tompkins_development.forge.spacecraft.block.entity.ModBlockEntities;
import com.tompkins_development.forge.spacecraft.entity.ModEntities;
import com.tompkins_development.forge.spacecraft.entity.client.MoonWolfRenderer;
import com.tompkins_development.forge.spacecraft.item.ModCreativeModeTabs;
import com.tompkins_development.forge.spacecraft.item.ModItems;
import com.tompkins_development.forge.spacecraft.networking.ModMessages;
import com.tompkins_development.forge.spacecraft.screen.ModMenuTypes;
import com.tompkins_development.forge.spacecraft.screen.oxygen.OxygenCollectorScreen;
import com.tompkins_development.forge.spacecraft.screen.oxygen.OxygenTankScreen;
import com.tompkins_development.forge.spacecraft.sounds.ModSounds;
import com.tompkins_development.forge.spacecraft.world.dimension.ModDimensions;
import com.tompkins_development.forge.spacecraft.world.structures.ModStructures;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SpacecraftMod.MOD_ID)
public class SpacecraftMod {

    public static final String MOD_ID = "spacecraft";

    public SpacecraftMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModStructures.register(modEventBus);
        ModSounds.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModDimensions.register();
        ModEntities.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModMessages.register();
        });
    }

    private void addCreative(CreativeModeTabEvent.BuildContents event) {
        if(event.getTab() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.RAW_TITANIUM);
            event.accept(ModItems.TITANIUM_INGOT);
        }

        if(event.getTab() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(ModBlocks.TITANIUM_BLOCK);
            event.accept(ModBlocks.MOON_STONE);
            event.accept(ModBlocks.OXYGEN_CABLE);
        }

        if(event.getTab() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.accept(ModBlocks.MOON_TITANIUM_ORE);
            event.accept(ModBlocks.MOON_GOLD_ORE);
//            event.accept(ModBlocks.ENDSTONE_BLACK_OPAL_ORE);
//            event.accept(ModBlocks.DEEPSLATE_BLACK_OPAL_ORE);
//            event.accept(ModBlocks.BLACK_OPAL_ORE);
        }



        if(event.getTab() == ModCreativeModeTabs.BLOCKS_TAB) {
            event.accept(ModItems.RAW_TITANIUM);
            event.accept(ModItems.TITANIUM_INGOT);
            event.accept(ModItems.OXYGEN_TANK_SMALL);
            event.accept(ModItems.OXYGEN_TANK_MEDIUM);
            event.accept(ModItems.OXYGEN_TANK_LARGE);
            event.accept(ModItems.OXYGEN_TANK_EMPTY);
            event.accept(ModBlocks.OXYGEN_CABLE);
            event.accept(ModBlocks.OXYGEN_TANK);

            event.accept(ModBlocks.MOON_STONE);
            event.accept(ModBlocks.MOON_TITANIUM_ORE);
            event.accept(ModBlocks.TITANIUM_BLOCK);
            event.accept(ModBlocks.MOON_GOLD_ORE);

            event.accept(ModBlocks.OXYGEN_COLLECTOR);

            event.accept(ModItems.SPACE_HELMET_V1);
            event.accept(ModItems.SPACE_CHESTPLATE_V1);
            event.accept(ModItems.SPACE_LEGS_V1);
            event.accept(ModItems.SPACE_BOOTS_V1);
//            event.accept(ModBlocks.ENDSTONE_BLACK_OPAL_ORE);
//            event.accept(ModBlocks.DEEPSLATE_BLACK_OPAL_ORE);
//            event.accept(ModBlocks.BLACK_OPAL_ORE);
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.MOON_WOLF.get(), MoonWolfRenderer::new);
            MenuScreens.register(ModMenuTypes.OXYGEN_COLLECTOR_MENU.get(), OxygenCollectorScreen::new);
            MenuScreens.register(ModMenuTypes.OXYGEN_TANK_MENU.get(), OxygenTankScreen::new);
        }
    }
}

//TODO List
/*
1. Remove Oxygen from cable and split evenly between each output/input
2. Make sure output/input wont go into itself
3. Make input/output from the smallest of the input/output rates
 */
