package com.tompkins_development.forge.spacecraft.world.features;


import com.tompkins_development.forge.spacecraft.SpacecraftMod;
import com.tompkins_development.forge.spacecraft.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class ModConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?,?>> MOON_TITANIUM_ORE = registerKey("moon_titanium_ore");
    public static final ResourceKey<ConfiguredFeature<?,?>> MOON_GOLD_ORE = registerKey("moon_gold_ore");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest moonStoneReplaceables = new BlockMatchTest(ModBlocks.MOON_STONE.get());

        List<OreConfiguration.TargetBlockState> moonTitaniumOre = List.of(OreConfiguration.target(moonStoneReplaceables,
                        ModBlocks.MOON_TITANIUM_ORE.get().defaultBlockState()));

        List<OreConfiguration.TargetBlockState> moonGoldOre = List.of(OreConfiguration.target(moonStoneReplaceables,
                ModBlocks.MOON_GOLD_ORE.get().defaultBlockState()));


        register(context, MOON_TITANIUM_ORE, Feature.ORE, new OreConfiguration(moonTitaniumOre, 9));
        register(context, MOON_GOLD_ORE, Feature.ORE, new OreConfiguration(moonGoldOre, 9));
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(SpacecraftMod.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
