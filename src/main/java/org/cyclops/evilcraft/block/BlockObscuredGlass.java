package org.cyclops.evilcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

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
    public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 10;
    }
}
