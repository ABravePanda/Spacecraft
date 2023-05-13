package com.tompkins_development.forge.spacecraft.item;

import com.tompkins_development.forge.spacecraft.SpacecraftMod;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SpacecraftMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeModeTabs {
    public static CreativeModeTab BLOCKS_TAB;
    public static CreativeModeTab OXYGEN_TAB;

    @SubscribeEvent
    public static void registerCreativeModeTabs(CreativeModeTabEvent.Register event) {
        BLOCKS_TAB = event.registerCreativeModeTab(new ResourceLocation(SpacecraftMod.MOD_ID, "space_blocks"),
                builder -> builder.icon(() -> new ItemStack(ModItems.TITANIUM_INGOT.get()))
                        .title(Component.translatable("creativemodetab.space_blocks")));
        OXYGEN_TAB = event.registerCreativeModeTab(new ResourceLocation(SpacecraftMod.MOD_ID, "oxygen_blocks"),
                builder -> builder.icon(() -> new ItemStack(ModItems.OXYGEN_TANK_SMALL.get()))
                        .title(Component.translatable("creativemodetab.oxygen_blocks")));
    }
}