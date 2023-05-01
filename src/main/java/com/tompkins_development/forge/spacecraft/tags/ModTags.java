package com.tompkins_development.forge.spacecraft.tags;

import com.tompkins_development.forge.spacecraft.SpacecraftMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static final TagKey<Block> OXYGEN_INPUT = BlockTags.create(new ResourceLocation(SpacecraftMod.MOD_ID, "oxygen_input"));
    public static final TagKey<Block> OXYGEN_OUTPUT = BlockTags.create(new ResourceLocation(SpacecraftMod.MOD_ID, "oxygen_output"));

}
