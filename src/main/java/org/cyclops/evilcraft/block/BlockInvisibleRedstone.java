package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.PushReaction;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import org.cyclops.cyclopscore.block.BlockTile;
import org.cyclops.evilcraft.tileentity.TileInvisibleRedstone;

/**
 * An invisible blockState where players can walk through and disappears after a few ticks.
 * @author immortaleeb
 *
 */
public class BlockInvisibleRedstone extends BlockTile {

    public BlockInvisibleRedstone(Block.Properties properties) {
        super(properties, TileInvisibleRedstone::new);
    }

    @Override
    public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return 15;
    }
    
    @Override
    public boolean canProvidePower(BlockState blockState) {
        return true;
    }

    @Override
    public PushReaction getPushReaction(BlockState blockState) {
        return PushReaction.BLOCK;
    }

    @Override
    public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
        return true;
    }

    @Override
    public boolean isReplaceable(BlockState p_225541_1_, Fluid p_225541_2_) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return VoxelShapes.empty();
    }
}
