package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import org.cyclops.evilcraft.item.ItemDarkGem;

/**
 * A storage blockState for {@link ItemDarkGem}.
 * @author rubensworks
 *
 */
public class BlockDark extends Block {

    public BlockDark(Block.Properties properties) {
        super(properties);
    }

    @Override
    public boolean isBeaconBase(BlockState state, IWorldReader world, BlockPos pos, BlockPos beacon) {
        return true;
    }

}
