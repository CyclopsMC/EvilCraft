package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

/**
 * Storage blockState for the dark power gem.
 * @author rubensworks
 *
 */
public class BlockDarkPowerGem extends Block {

    public BlockDarkPowerGem(Block.Properties properties) {
        super(properties);
    }

    @Override
    public boolean isBeaconBase(BlockState state, IWorldReader world, BlockPos pos, BlockPos beacon) {
        return true;
    }

}
