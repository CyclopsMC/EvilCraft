package org.cyclops.evilcraft.block;

import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

/**
 * Glass that holds back some light.
 * @author rubensworks
 *
 */
public class BlockObscuredGlass extends AbstractGlassBlock {

    public BlockObscuredGlass(Block.Properties properties) {
        super(properties);
    }

    @Override
    public int getLightBlock(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 10;
    }
}
