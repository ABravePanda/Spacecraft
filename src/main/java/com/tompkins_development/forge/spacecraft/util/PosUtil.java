package com.tompkins_development.forge.spacecraft.util;

import com.tompkins_development.forge.spacecraft.block.entity.IOxygenBlockEntity;
import com.tompkins_development.forge.spacecraft.block.entity.OxygenCableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;

import java.util.ArrayList;
import java.util.List;

public class PosUtil {

    public static List<Neighbor> getNeighboringBlocks(Level level, BlockPos pos) {
        List<Neighbor> neighbors = new ArrayList<>();
        neighbors.add(new Neighbor(level, pos.north(), Neighbor.Direction.NORTH));
        neighbors.add(new Neighbor(level, pos.south(), Neighbor.Direction.SOUTH));
        neighbors.add(new Neighbor(level, pos.east(), Neighbor.Direction.EAST));
        neighbors.add(new Neighbor(level, pos.west(), Neighbor.Direction.WEST));
        neighbors.add(new Neighbor(level, pos.above(), Neighbor.Direction.TOP));
        neighbors.add(new Neighbor(level, pos.below(), Neighbor.Direction.BOTTOM));
        return neighbors;
    }

    public static List<Neighbor> getNeighboringBlocksByType(Level level, BlockPos pos, Block block) {
        List<Neighbor> neighbors = new ArrayList<>();
        if(level.getBlockState(pos.north()).getBlock() == block)
            neighbors.add(new Neighbor(level, pos.north(), Neighbor.Direction.NORTH));
        if(level.getBlockState(pos.south()).getBlock() == block)
        neighbors.add(new Neighbor(level, pos.south(), Neighbor.Direction.SOUTH));
        if(level.getBlockState(pos.east()).getBlock() == block)
        neighbors.add(new Neighbor(level, pos.east(), Neighbor.Direction.EAST));
        if(level.getBlockState(pos.west()).getBlock() == block)
        neighbors.add(new Neighbor(level, pos.west(), Neighbor.Direction.WEST));
        if(level.getBlockState(pos.above()).getBlock() == block)
        neighbors.add(new Neighbor(level, pos.above(), Neighbor.Direction.TOP));
        if(level.getBlockState(pos.below()).getBlock() == block)
        neighbors.add(new Neighbor(level, pos.below(), Neighbor.Direction.BOTTOM));
        return neighbors;
    }

    public static List<Neighbor> getNeighboringBlocksForOxygen(Level level, BlockPos pos) {
        List<Neighbor> neighbors = new ArrayList<>();
        if(level.getBlockEntity(pos.north()) instanceof IOxygenBlockEntity)
            neighbors.add(new Neighbor(level, pos.north(), Neighbor.Direction.NORTH));
        if(level.getBlockEntity(pos.south()) instanceof IOxygenBlockEntity)
            neighbors.add(new Neighbor(level, pos.south(), Neighbor.Direction.SOUTH));
        if(level.getBlockEntity(pos.east()) instanceof IOxygenBlockEntity)
            neighbors.add(new Neighbor(level, pos.east(), Neighbor.Direction.EAST));
        if(level.getBlockEntity(pos.west()) instanceof IOxygenBlockEntity)
            neighbors.add(new Neighbor(level, pos.west(), Neighbor.Direction.WEST));
        if(level.getBlockEntity(pos.above()) instanceof IOxygenBlockEntity)
            neighbors.add(new Neighbor(level, pos.above(), Neighbor.Direction.TOP));
        if(level.getBlockEntity(pos.below()) instanceof IOxygenBlockEntity)
            neighbors.add(new Neighbor(level, pos.below(), Neighbor.Direction.BOTTOM));
        return neighbors;
    }

    public static boolean hasSameNeighbor(Level level, BlockPos pos, Block block) {
        List<Neighbor> neighbors = getNeighboringBlocks(level, pos);
        for(Neighbor neighbor : neighbors)
            if(neighbor.getBlock() == block) return true;
        return false;
    }

    public static boolean conflictingMasters(Level level, BlockPos pos) {
        BlockPos master = null;
        List<Neighbor> neighbors = getNeighboringBlocks(level, pos);
        for(Neighbor neighbor : neighbors)
            if(neighbor.getBlockEntity() instanceof OxygenCableBlockEntity) {
                OxygenCableBlockEntity neighborEntity = (OxygenCableBlockEntity) neighbor.getBlockEntity();
                if(master == null) master = neighborEntity.getBlockPos();
                else if(master != neighborEntity.getMaster(neighborEntity)) return true;
            }
        return false;
    }

    public static OxygenCableBlockEntity getMasterCable(Level level, BlockPos pos) {
        return (OxygenCableBlockEntity) level.getBlockEntity(pos);
    }
}
