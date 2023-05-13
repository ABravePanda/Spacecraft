package com.tompkins_development.forge.spacecraft.block;

import com.tompkins_development.forge.spacecraft.block.entity.IOxygenDirectional;
import com.tompkins_development.forge.spacecraft.block.entity.ModBlockEntities;
import com.tompkins_development.forge.spacecraft.block.entity.OxygenTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;


public class OxygenTankBlock extends BaseEntityBlock {

    public static final EnumProperty<Direction> outputDirection = EnumProperty.create("outputdirection", Direction.class);
    public static final EnumProperty<Direction> inputDirection = EnumProperty.create("inputdirection", Direction.class);

    public OxygenTankBlock() {
        super(BlockBehaviour.Properties.of(Material.STONE)
                .strength(3f, 3f));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(outputDirection, Direction.NORTH)
                .setValue(inputDirection, Direction.SOUTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(outputDirection);
        builder.add(inputDirection);
    }

    /* Block Entity */

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState state) {
        return new OxygenTankBlockEntity(blockPos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }


    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if(!level.isClientSide()) {
            BlockEntity entity = level.getBlockEntity(blockPos);
            if(entity instanceof OxygenTankBlockEntity)
                NetworkHooks.openScreen((ServerPlayer) player, (OxygenTankBlockEntity) entity, blockPos);
            else
                throw new IllegalStateException("Container Provider Missing For Oxygen Tank");
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntities.OXYGEN_TANK.get(), OxygenTankBlockEntity::tick);
    }
}
