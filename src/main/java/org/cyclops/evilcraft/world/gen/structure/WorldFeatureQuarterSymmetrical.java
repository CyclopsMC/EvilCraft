package org.cyclops.evilcraft.world.gen.structure;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

/**
 * A quarter of a structure that can be rotated three times.
 * @author immortaleeb
 *
 */
public abstract class WorldFeatureQuarterSymmetrical extends Feature<NoFeatureConfig> {
	private List<Integer> layerHeights;
	private List<BlockWrapper[]> layers;

	// Width and height of a quarter of the structure
	protected int quarterWidth;
	protected int quarterHeight;

	public WorldFeatureQuarterSymmetrical(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn, int quarterWidth, int quarterHeight) {
		super(configFactoryIn);
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

	protected void buildCorner(IWorld world, BlockPos blockPos, int incX, int incZ) {
		Random r = new Random();
		for (int i = 0; i < layerHeights.size(); ++i) {
			int layerHeight = layerHeights.get(i);
			BlockWrapper[] layer = layers.get(i);

			// Don't overwrite the borders everytime we place blocks
			int start = (incX == incZ) ? 0 : 1;

			for (int zr = start; zr < quarterHeight; ++zr) {
				for (int xr = start; xr < quarterWidth; ++xr) {
					BlockWrapper wrapper = layer[(quarterWidth - xr - 1) * quarterHeight + zr];
					BlockPos posOffset = blockPos.add(xr * incX, layerHeight, zr * incZ);
					if (wrapper != null && (wrapper.chance > 0 && wrapper.chance >= r.nextFloat())) { // not an air blockState?
						world.setBlockState(blockPos.add(xr * incX, layerHeight, zr * incZ), wrapper.blockState, MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
						if(wrapper.action != null) {
							wrapper.action.run(world, posOffset);
						}
					}

				}
			}
		}
		
		postBuildCorner(world, blockPos, incX, incZ);
	}

	protected void postBuildCorner(IWorld world, BlockPos pos, int incX, int incZ) {
		for (int i = 0; i < layerHeights.size(); ++i) {
			int layerHeight = layerHeights.get(i);
			BlockWrapper[] layer = layers.get(i);

			// Don't overwrite the borders everytime we place blocks
			int start = (incX == incZ) ? 0 : 1;

			for (int zr = start; zr < quarterHeight; ++zr) {
				for (int xr = start; xr < quarterWidth; ++xr) {
					BlockWrapper wrapper = layer[(quarterWidth - xr - 1) * quarterHeight + zr];
					if (wrapper != null) {
						BlockPos posOffset = pos.add(xr * incX, layerHeight, zr * incZ);
						if(wrapper.action != null && world.getBlockState(posOffset) == wrapper.blockState) {
							wrapper.action.run(world, posOffset);
						}
					}
				}
			}
		}
	}

	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos blockPos, NoFeatureConfig config) {
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
		 * {@link BlockState} for which this instance is a wrapper.
		 */
		public BlockState blockState;
		/**
		 * Chance of spawning
		 */
		public float chance = 1;
		/**
		 * An optional action after a block has been placed.
		 */
		public IBlockAction action = null;
		
		/**
		 * Creates a new wrapper around the specified {@link Block} with metadata 0.
		 * @param block The blockState to wrap.
		 */
		public BlockWrapper(Block block) {
			this(block.getDefaultState());
		}
		
		/**
		 * Creates a new wrapper around the specified {@link BlockState} with the specified metadata.
		 * @param blockState The blockState to wrap.
		 */
		public BlockWrapper(BlockState blockState) {
			this.blockState = blockState;
		}

		/**
		 * Creates a new wrapper around the specified {@link Block} with the specified metadata and a certain chance.
		 * @param blockState The block to wrap.
		 * @param chance The chance on spawning.
		 */
		public BlockWrapper(BlockState blockState, float chance) {
			this(blockState);
			this.chance = chance;
		}
	}

	public static interface IBlockAction {

		public void run(IWorld world, BlockPos pos);

	}

}
