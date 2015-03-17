package evilcraft.core.block.component;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Interface for the {@link EntityDropParticleFXBlockComponent}.
 * @author rubensworks
 *
 */
public interface IEntityDropParticleFXBlock {
    /**
     * Called when a random display tick occurs.
     * @param world The world.
     * @param blockPos The position.
     * @param blockState The block state.
     * @param rand Random object.
     */
    public void randomDisplayTick(World world, BlockPos blockPos, IBlockState blockState, Random rand);
}
