package evilcraft.worldgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.world.World;

public abstract class QuarterSymmetricalStructure {
	private List<Integer> layerHeights;
	private List<int[]> layers;
	private List<int[]> metadataIds;

	// Width and height of a quarter of the structure
	protected int quarterWidth;
	protected int quarterHeight;

	public QuarterSymmetricalStructure(int quarterWidth, int quarterHeight) {
		layerHeights = new ArrayList<Integer>();
		layers = new ArrayList<int[]>();
		metadataIds = new ArrayList<int[]>();

		this.quarterWidth = quarterWidth;
		this.quarterHeight = quarterHeight;

		generateLayers();
	}

	protected abstract void generateLayers();

	protected void addLayer(int height, int[] layer) {
		layerHeights.add(height);
		layers.add(layer);
	}

	protected int getBlockIdWithMetadata(int blockId, int metadata) {
		metadataIds.add(new int[] { blockId, metadata });
		return -metadataIds.size();
	}

	protected int[] getBlockAndMetadata(int metadataBlockId) {
		return metadataIds.get(-metadataBlockId - 1);
	}

	protected void buildCorner(World world, int x, int y, int z, int incX, int incZ) {
		for (int i = 0; i < layerHeights.size(); ++i) {
			int layerHeight = layerHeights.get(i);
			int[] layer = layers.get(i);

			// Don't overwrite the borders everytime we place blocks
			int start = (incX == incZ) ? 0 : 1;

			for (int zr = start; zr < quarterHeight; ++zr) {
				for (int xr = start; xr < quarterWidth; ++xr) {
					int id = layer[(quarterWidth - xr - 1) * quarterHeight + zr];
					int metadata = 0;

					if (id < 0) {
						int[] data = getBlockAndMetadata(id);
						id = data[0];
						metadata = data[1];
					}

					if (id != 0) // not an air block?
						world.setBlock(x + xr * incX, y + layerHeight, z + zr * incZ, id, metadata, 2);
				}
			}
		}
		
		postBuildCorner(world, x, y, z, incX, incZ);
	}
	
	protected void postBuildCorner(World world, int x, int y, int z, int incX, int incZ) {
		
	}

	public boolean generate(World world, Random random, int x, int y, int z) {
		buildCorner(world, x, y, z, 1, 1);
		buildCorner(world, x, y, z, -1, 1);
		buildCorner(world, x, y, z, 1, -1);
		buildCorner(world, x, y, z, -1, -1);

		return true;
	}
}
