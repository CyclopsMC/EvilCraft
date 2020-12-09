package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.block.BlockTile;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.tileentity.TileEnvironmentalAccumulator;
import org.cyclops.evilcraft.world.gen.decorator.WorldDecoratorDarkTemple;

/**
 * Block that can collect the weather and stuff.
 * @author immortaleeb
 *
 */
public class BlockEnvironmentalAccumulator extends BlockTile {
    
    /**
     * State indicating the environmental accumulator is idle.
     */
    public static final int STATE_IDLE = 0;
    /**
     * State indicating the environmental accumulator is currently
     * processing an item.
     */
    public static final int STATE_PROCESSING_ITEM = 1;
    /**
     * State indicating the environmental accumulator is cooling
     * down.
     */
    public static final int STATE_COOLING_DOWN = 2;
    /**
     * State indicating the environmental accumulator has just
     * finished processing an item. This state is necessary
     * because using this state we can put some delay between
     * processing an item and cooling down so that the client
     * gets a moment to show an effect when an item has finished
     * processing.
     */
    public static final int STATE_FINISHED_PROCESSING_ITEM = 3;

	public BlockEnvironmentalAccumulator(Block.Properties properties) {
		super(properties, TileEnvironmentalAccumulator::new);
	}

	@Override
	public void onPlayerDestroy(IWorld worldIn, BlockPos pos, BlockState state) {
		super.onPlayerDestroy(worldIn, pos, state);
		remove(worldIn, pos);
	}

	@Override
	public void onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosionIn) {
		super.onExplosionDestroy(worldIn, pos, explosionIn);
		remove(worldIn, pos);
	}

	protected void remove(IWorld world, BlockPos blockPos) {
		if(!world.isRemote()) {
			BlockPos closest = WorldDecoratorDarkTemple.getClosestForCoords(world, blockPos.getX(), blockPos.getZ());
			if(closest != null) {
				EvilCraft.darkTempleData.addFailedLocation(world.getDimension(), closest.getX() / WorldHelpers.CHUNK_SIZE, closest.getZ() / WorldHelpers.CHUNK_SIZE);
			}
		}
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
		return TileHelpers.getSafeTile(worldIn, pos, TileEnvironmentalAccumulator.class)
				.map(tile -> tile.getState() == STATE_IDLE ? 15 : 0)
				.orElse(0);
	}
}
