package com.tompkins_development.forge.spacecraft.world.dimension;

import com.tompkins_development.forge.spacecraft.SpacecraftMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModDimensions {

    public static final ResourceKey<Level> MOON_KEY = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(SpacecraftMod.MOD_ID, "moon"));
    public static final ResourceKey<DimensionType> MOON = ResourceKey.create(Registries.DIMENSION_TYPE, MOON_KEY.registry());


    public static void register() {
        System.out.println("Registering Dimension for " + SpacecraftMod.MOD_ID);
    }
}