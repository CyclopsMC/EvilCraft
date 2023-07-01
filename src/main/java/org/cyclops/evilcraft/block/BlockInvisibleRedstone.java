package org.cyclops.evilcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.cyclops.cyclopscore.block.BlockWithEntity;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityInvisibleRedstone;

import javax.annotation.Nullable;

/**
 * An invisible blockState where players can walk through and disappears after a few ticks.
 * @author immortaleeb
 *
 */
public class BlockInvisibleRedstone extends BlockWithEntity {

    public BlockInvisibleRedstone(Block.Properties properties) {
        super(properties, BlockEntityInvisibleRedstone::new);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, RegistryEntries.BLOCK_ENTITY_INVISIBLE_REDSTONE, new BlockEntityInvisibleRedstone.TickerServer());
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return 15;
    }

    @Override
    public boolean isSignalSource(BlockState blockState) {
        return true;
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return true;
    }

    @Override
    public boolean canBeReplaced(BlockState p_225541_1_, Fluid p_225541_2_) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }
}
