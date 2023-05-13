package com.tompkins_development.forge.spacecraft.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class Neighbor {

    private Level level;
    private BlockPos pos;
    private Direction direction;

    public Neighbor(Level level, BlockPos pos, Direction direction) {
        this.level = level;
        this.direction = direction;
        this.pos = pos;
    }

    public Level getLevel() {
        return level;
    }

    public BlockPos getPos() {
        return pos;
    }

    public Direction getDirection() {
        return direction;
    }

    public BlockState getBlockState() {
        return getLevel().getBlockState(getPos());
    }

    public Block getBlock() {
        return getBlockState().getBlock();
    }

    public BlockEntity getBlockEntity() {
        return getLevel().getBlockEntity(getPos());
    }

}
