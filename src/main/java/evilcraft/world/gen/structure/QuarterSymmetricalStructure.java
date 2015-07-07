package evilcraft.world.gen.structure;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A quarter of a structure that can be rotated three times.
 * @author immortaleeb
 *
 */
public abstract class QuarterSymmetricalStructure {
	private List<Integer> layerHeights;
	private List<BlockWrapper[]> layers;

	// Width and height of a quarter of the structure
	protected int quarterWidth;
	protected int quarterHeight;

	/**
	 * Make a new instance with the given dimensions (only one height layer).
	 * @param quarterWidth Width of the quarter.
	 * @param quarterHeight Height of the quarter.
	 */
	public QuarterSymmetricalStructure(int quarterWidth, int quarterHeight) {
		layerHeights = new ArrayList<Integer>();
		layers = new ArrayList<BlockWrapper[]>();

		this.quarterWidth = quarterWidth;
		this.quarterHeight = quarterHeight;

		generateLayers();
	}

	protected abstract void generateLayers();

	protected void addLayer(int height, BlockWrapper[] layer) {
		layerHeights.add(height);
		layers.add(layer);
	}

	protected void buildCorner(World world, BlockPos blockPos, int incX, int incZ) {
		for (int i = 0; i < layerHeights.size(); ++i) {
			int layerHeight = layerHeights.get(i);
			BlockWrapper[] layer = layers.get(i);

			// Don't overwrite the borders everytime we place blocks
			int start = (incX == incZ) ? 0 : 1;

			for (int zr = start; zr < quarterHeight; ++zr) {
				for (int xr = start; xr < quarterWidth; ++xr) {
					BlockWrapper wrapper = layer[(quarterWidth - xr - 1) * quarterHeight + zr];

					if (wrapper != null) // not an air blockState?
						world.setBlockState(blockPos.add(xr * incX, layerHeight, zr * incZ), wrapper.blockState, MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
				}
			}
		}
		
		postBuildCorner(world, blockPos, incX, incZ);
	}
	
	protected void postBuildCorner(World world, BlockPos blockPos, int incX, int incZ) {
		
	}

	/**
	 * Generate this structure.
	 * @param world The world.
	 * @param random Random object.
	 * @param blockPos Center coordinate.
	 * @return If the structure was generated.
	 */
	public boolean generate(World world, Random random, BlockPos blockPos) {
		buildCorner(world, blockPos, 1, 1);
		buildCorner(world, blockPos, -1, 1);
		buildCorner(world, blockPos, 1, -1);
		buildCorner(world, blockPos, -1, -1);

		return true;
	}
	
	/**
	 * This is a wrapper class, which wraps around a {@link Block} and
	 * pairs with it the metadata for that specific blockState instance.
	 * 
	 * @author immortaleeb
	 *
	 */
	public class BlockWrapper {
		/**
		 * {@link IBlockState} for which this instance is a wrapper.
		 */
		public IBlockState blockState;
		
		/**
		 * Creates a new wrapper around the specified {@link Block} with metadata 0.
		 * @param block The blockState to wrap.
		 */
		public BlockWrapper(Block block) {
			this(block.getDefaultState());
		}
		
		/**
		 * Creates a new wrapper around the specified {@link IBlockState} with the specified metadata.
		 * @param blockState The blockState to wrap.
		 */
		public BlockWrapper(IBlockState blockState) {
			this.blockState = blockState;
		}
	}
}
