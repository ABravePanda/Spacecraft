package com.tompkins_development.forge.spacecraft.block;

import com.tompkins_development.forge.spacecraft.block.entity.IOxygenBlockEntity;
import com.tompkins_development.forge.spacecraft.block.entity.ModBlockEntities;
import com.tompkins_development.forge.spacecraft.block.entity.OxygenCableBlockEntity;
import com.tompkins_development.forge.spacecraft.capabilities.ModCapabilities;
import com.tompkins_development.forge.spacecraft.util.Neighbor;
import com.tompkins_development.forge.spacecraft.util.PosUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OxygenCableBlock extends BaseEntityBlock {

    private static final BooleanProperty NORTH = BooleanProperty.create("north");
    private static final BooleanProperty EAST = BooleanProperty.create("east");
    private static final BooleanProperty SOUTH = BooleanProperty.create("south");
    private static final BooleanProperty WEST = BooleanProperty.create("west");
    private static final BooleanProperty UP = BooleanProperty.create("up");
    private static final BooleanProperty DOWN = BooleanProperty.create("down");

    private static final VoxelShape SHAPE_CORE = box(6, 6, 6, 10, 10, 10);
    private static final VoxelShape SHAPE_NORTH = box(6, 6, 0, 10, 10, 6);
    private static final VoxelShape SHAPE_EAST = box(10, 6, 6, 16, 10, 10);
    private static final VoxelShape SHAPE_SOUTH = box(6, 6, 10, 10, 10, 16);
    private static final VoxelShape SHAPE_WEST = box(0, 6, 6, 6, 10, 10);
    private static final VoxelShape SHAPE_UP = box(6, 10, 6, 10, 16, 10);
    private static final VoxelShape SHAPE_DOWN = box(6, 0, 6, 10, 6, 10);


    public OxygenCableBlock() {
        super(BlockBehaviour.Properties.of(Material.STONE)
                .strength(3f, 3f).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, true)
                .setValue(WEST, false)
                .setValue(UP, false)
                .setValue(DOWN, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH);
        builder.add(EAST);
        builder.add(SOUTH);
        builder.add(WEST);
        builder.add(UP);
        builder.add(DOWN);
    }

//    @Nullable
//    @Override
//    public BlockState getStateForPlacement(BlockPlaceContext context) {
//        return this.defaultBlockState()
//                .setValue(NORTH, false)
//                .setValue(EAST, false)
//                .setValue(SOUTH, true)
//                .setValue(WEST, false)
//                .setValue(UP, false)
//                .setValue(DOWN, false);
//    }


//    @Override
//    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
//        if(levelAccessor.getBlockEntity(neighbor) instanceof IOxygenBlockEntity) {
//            System.out.println("block entity");
//            System.out.println(facing);
//            BlockState newState = levelAccessor.getBlockState(pos);
//            switch (facing) {
//                case NORTH -> newState.setValue(NORTH, true);
//                case EAST -> newState.setValue(EAST, true);
//                case SOUTH -> newState.setValue(SOUTH, true);
//                case WEST -> newState.setValue(WEST, true);
//                case UP -> newState.setValue(UP, true);
//                case DOWN -> newState.setValue(DOWN, true);
//            }
//
//            System.out.println(newState);
//            return newState;
//        }
//        return super.updateShape(state, direction, state1, levelAccessor, pos, neighbor);
//    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockGetter blockgetter = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockPos blockpos1 = blockpos.north();
        BlockPos blockpos2 = blockpos.east();
        BlockPos blockpos3 = blockpos.south();
        BlockPos blockpos4 = blockpos.west();
        BlockState blockstate = blockgetter.getBlockState(blockpos1);
        BlockState blockstate1 = blockgetter.getBlockState(blockpos2);
        BlockState blockstate2 = blockgetter.getBlockState(blockpos3);
        BlockState blockstate3 = blockgetter.getBlockState(blockpos4);
        return super.getStateForPlacement(context)
                .setValue(NORTH, Boolean.valueOf(this.connectsTo(context.getLevel(), blockpos1, blockstate, Direction.SOUTH)))
                .setValue(EAST, Boolean.valueOf(this.connectsTo(context.getLevel(), blockpos2,blockstate1, Direction.WEST)))
                .setValue(SOUTH, Boolean.valueOf(this.connectsTo(context.getLevel(), blockpos3,blockstate2, Direction.NORTH)))
                .setValue(WEST, Boolean.valueOf(this.connectsTo(context.getLevel(), blockpos4,blockstate3,  Direction.EAST)));
    }

    public boolean connectsTo(Level level,  BlockPos pos, BlockState blockState, Direction direction) {
        boolean flag = level.getBlockEntity(pos) instanceof IOxygenBlockEntity;
        return flag;
    }


    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighbor, boolean notify) {
        if(level.isClientSide) return;
        //System.out.println("called");
        boolean value = level.getBlockEntity(neighbor) instanceof IOxygenBlockEntity;
            //System.out.println("block entity");
        Direction direction = getDirection(pos, neighbor);
        BooleanProperty property = NORTH;

        if(direction == Direction.NORTH)
            property = NORTH;
        if(direction == Direction.EAST)
            property = EAST;
        if(direction == Direction.SOUTH)
            property = SOUTH;
        if(direction == Direction.WEST)
            property = WEST;
        if(direction == Direction.UP)
            property = UP;
        if(direction == Direction.DOWN)
            property = DOWN;

        level.setBlockAndUpdate(pos,state.setValue(property,value));
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return getCableShape(p_60555_);
    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(state, level, pos, neighbor);
    }

    private Direction getDirection(BlockPos pos, BlockPos neighbor) {
        if(pos.offset(Direction.NORTH.getNormal()).equals(neighbor)) return Direction.NORTH;
        if(pos.offset(Direction.SOUTH.getNormal()).equals(neighbor)) return Direction.SOUTH;
        if(pos.offset(Direction.EAST.getNormal()).equals(neighbor)) return Direction.EAST;
        if(pos.offset(Direction.WEST.getNormal()).equals(neighbor)) return Direction.WEST;
        if(pos.offset(Direction.UP.getNormal()).equals(neighbor)) return Direction.UP;
        if(pos.offset(Direction.DOWN.getNormal()).equals(neighbor)) return Direction.DOWN;
        return Direction.DOWN;
    }

    protected static VoxelShape getCableShape(BlockState state) {
        VoxelShape shape = SHAPE_CORE;

        if (Boolean.TRUE.equals(state.getValue(NORTH))) {
            shape = Shapes.or(shape, SHAPE_NORTH);
        }

        if (Boolean.TRUE.equals(state.getValue(EAST))) {
            shape = Shapes.or(shape, SHAPE_EAST);
        }

        if (Boolean.TRUE.equals(state.getValue(SOUTH))) {
            shape = Shapes.or(shape, SHAPE_SOUTH);
        }

        if (Boolean.TRUE.equals(state.getValue(WEST))) {
            shape = Shapes.or(shape, SHAPE_WEST);
        }

        if (Boolean.TRUE.equals(state.getValue(UP))) {
            shape = Shapes.or(shape, SHAPE_UP);
        }

        if (Boolean.TRUE.equals(state.getValue(DOWN))) {
            shape = Shapes.or(shape, SHAPE_DOWN);
        }

        return shape;
    }


    /* --- */


    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newBlockState, boolean isMoving) {
        OxygenCableBlockEntity oxygenCable = (OxygenCableBlockEntity) level.getBlockEntity(blockPos);
        oxygenCable.clearOutputs(oxygenCable);
        oxygenCable.clearInputs(oxygenCable);
        OxygenCableBlockEntity master = (OxygenCableBlockEntity) level.getBlockEntity(oxygenCable.getMaster(oxygenCable));
        super.onRemove(blockState, level, blockPos, newBlockState, isMoving);
        if(!PosUtil.hasSameNeighbor(level, blockPos, ModBlocks.OXYGEN_CABLE.get())) return;

        //See if neighboring cables are still attached (if one of the cables is the same as the master pos), if not then create a new master and set all old cables to new master
        List<Neighbor> neighborList = PosUtil.getNeighboringBlocks(level, blockPos);
        for(Neighbor neighbor : neighborList) {
            if(neighbor.getBlockEntity() instanceof OxygenCableBlockEntity) {
                OxygenCableBlockEntity neighborEntity = (OxygenCableBlockEntity) neighbor.getBlockEntity();
                BlockPos masterPosition = neighborEntity.getMaster(neighborEntity);
                if(!masterConnectionExists(level, neighborEntity, blockPos, masterPosition)) {
                    overrideConnectedMasters(level, neighborEntity.getBlockPos(), neighborEntity.getBlockPos());
                }
            }
            assert master != null;
            master.clearInputs(master);
            master.clearOutputs(master);
        }
    }


    @Override
    public void onPlace(BlockState state, Level level, BlockPos blockPos, BlockState newBlockState, boolean isMoving) {
        super.onPlace(state, level, blockPos, newBlockState, isMoving);
        if(!PosUtil.hasSameNeighbor(level, blockPos, ModBlocks.OXYGEN_CABLE.get())) {
            OxygenCableBlockEntity entity = (OxygenCableBlockEntity) level.getBlockEntity(blockPos);
            entity.setMaster(entity, entity.getBlockPos());
            return;
        }

        List<Neighbor> neighborList = PosUtil.getNeighboringBlocks(level, blockPos);
        for(Neighbor neighbor : neighborList) {
            //If block has a neighbor already set the placed entities master to be the neighbors master
            if(neighbor.getBlock() == ModBlocks.OXYGEN_CABLE.get() && neighbor.getBlockEntity() instanceof OxygenCableBlockEntity) {
                OxygenCableBlockEntity neighborEntity = (OxygenCableBlockEntity) neighbor.getBlockEntity();
                OxygenCableBlockEntity currentEntity = (OxygenCableBlockEntity) level.getBlockEntity(blockPos);

                if(PosUtil.conflictingMasters(level, blockPos)) {
                    overrideConnectedMasters(level, currentEntity, neighborEntity.getMaster(neighborEntity));
                }
                currentEntity.setMaster(currentEntity, neighborEntity.getMaster(neighborEntity));
            }
        }
    }


    private boolean masterConnectionExists(Level level, BlockPos neighborPos, BlockPos startingPos, BlockPos masterPosition, Set<BlockPos> visited) {
        List<Neighbor> neighbors = PosUtil.getNeighboringBlocksByType(level, neighborPos, ModBlocks.OXYGEN_CABLE.get());
        for (Neighbor neighbor : neighbors) {
            BlockPos neighborBlockPos = neighbor.getPos();
            if (!(neighbor.getBlockEntity() instanceof OxygenCableBlockEntity)) continue;
            OxygenCableBlockEntity newNeighborEntity = (OxygenCableBlockEntity) neighbor.getBlockEntity();
            if (newNeighborEntity.getBlockPos().equals(startingPos)) continue;
            if (newNeighborEntity.getBlockPos().equals(masterPosition)) return true;
            if (!visited.contains(neighborBlockPos)) {
                visited.add(neighborBlockPos);
                if (masterConnectionExists(level, neighborBlockPos, startingPos, masterPosition, visited)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void overrideConnectedMasters(Level level, BlockPos brokenPos, BlockPos newMaster) {
        List<Neighbor> neighbors = PosUtil.getNeighboringBlocks(level, brokenPos);
        for (Neighbor neighbor : neighbors) {
            if (neighbor.getBlockEntity() instanceof OxygenCableBlockEntity) {
                OxygenCableBlockEntity neighborEntity = (OxygenCableBlockEntity) neighbor.getBlockEntity();
                if (neighborEntity.getMaster(neighborEntity).equals(newMaster)) continue;
                neighborEntity.setMaster(neighborEntity, newMaster);
                OxygenCableBlockEntity newMasterEntity = (OxygenCableBlockEntity) level.getBlockEntity(newMaster);
                newMasterEntity.clearInputs(newMasterEntity);
                newMasterEntity.clearOutputs(newMasterEntity);
                overrideConnectedMasters(level, neighborEntity, newMaster);
            }
        }
    }

    private void overrideConnectedMasters(Level level, OxygenCableBlockEntity brokenEntity, BlockPos newMaster) {
        overrideConnectedMasters(level, brokenEntity.getBlockPos(), newMaster);
    }

    private boolean masterConnectionExists(Level level, OxygenCableBlockEntity neighborEntity, BlockPos startingPos, BlockPos masterPosition) {
        return masterConnectionExists(level, neighborEntity.getBlockPos(), startingPos, masterPosition, new HashSet<>());
    }


    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if(!(level.getBlockEntity(blockPos) instanceof OxygenCableBlockEntity)) return InteractionResult.FAIL;
        if(interactionHand != InteractionHand.MAIN_HAND) return InteractionResult.FAIL;
        if(level.isClientSide()) return InteractionResult.FAIL;

        OxygenCableBlockEntity entity = (OxygenCableBlockEntity) level.getBlockEntity(blockPos);
        player.sendSystemMessage(Component.literal("Location: " + entity.getBlockPos()));
        player.sendSystemMessage(Component.literal("Master: " + entity.getMaster(entity)));
        player.sendSystemMessage(Component.literal("Inputs: " + entity.getInputs(entity).size()));
        player.sendSystemMessage(Component.literal("Outputs: " + entity.getOutputs(entity).size()));
        player.sendSystemMessage(Component.literal("------"));
        entity.getCapability(ModCapabilities.OXYGEN).ifPresent((handler) -> {
            player.sendSystemMessage(Component.literal("Oxygen: " + handler.getOxygenStored()));
        });

        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    /* Block Entity */
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState state) {
        return new OxygenCableBlockEntity(blockPos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }


    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntities.OXYGEN_CABLE.get(), OxygenCableBlockEntity::tick);
    }
}