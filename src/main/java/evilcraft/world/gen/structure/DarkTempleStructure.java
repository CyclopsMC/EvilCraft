package evilcraft.world.gen.structure;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import evilcraft.GeneralConfig;
import evilcraft.block.EnvironmentalAccumulator;
import evilcraft.core.helper.DirectionHelpers;
import evilcraft.core.helper.StairSlabMetadataHelper;
import evilcraft.core.helper.StairSlabMetadataHelper.SlabType;
import evilcraft.core.helper.StairSlabMetadataHelper.StoneBrickType;
import evilcraft.world.gen.nbt.DarkTempleData;

/**
 * Structure which generates Dark Temples.
 * 
 * @author immortaleeb
 *
 */
public class DarkTempleStructure extends QuarterSymmetricalStructure {
	private static final int STRUCTURE_HEIGHT = 9;

	private static final int[] CORNER_INC = {-1, 1};
	
	private static DarkTempleStructure _instance = null;
	
	private DarkTempleData darkTempleData;

	/**
	 * Get the unique instance.
	 * @param darkTempleData The data of the temple.
	 * @return Unique instance.
	 */
	public static DarkTempleStructure getInstance(DarkTempleData darkTempleData) {
		if (_instance == null)
			_instance = new DarkTempleStructure(darkTempleData);
		
		return _instance;
	}
	
	private DarkTempleStructure(DarkTempleData darkTempleData) {
		super(6, 6);
		this.darkTempleData = darkTempleData;
	}
	
	/**
	 * Finds the y coordinate of a block that is at ground-level
	 * given its x and z coordinates, between yMin and yMax
	 * (borders included) and that is a valid block to place a
	 * dark temple on.
	 * 
	 * @param world The world.
	 * @param x x-coordinate.
	 * @param z y-coordinate.
	 * @return The y-coordinate of a block on the ground, or -1 if no y was found
	 *         in the range yMin to yMax (borders included).
	 */
	private int findGround(World world, int x, int z, int yMin, int yMax) {
		if (yMin <= yMax) {
			int height = yMax;
			
			// Try all possible y-coordinates from yMax to yMin
			while (height >= yMin) {
				// Look for the first block that is not an air block and has an air block above it
				while (height >= yMin && (world.isAirBlock(x, height, z) || !world.isAirBlock(x, height+1, z))) {
					height--;
				}
				
				// Is it a valid spot to place the center of a dark temple on?
				if (height >= yMin && isValidSpot(world, x, height, z))
					return height;
				
				height--;
			}
		}
		
		return -1;
	}
	
	private boolean canPlaceStructure(World world, int x, int y, int z) {
		for (int xr = x - 3; xr <= x + 3; xr++) {
			for (int yr = y; yr <= y + 9; yr++) {
				for (int zr = z - 3; zr <= z + 3; zr++) {
					if (isSolidBlock(world, xr, yr, zr)) {
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
	private boolean isValidSpot(World world, int x, int y, int z) {
		Block block = world.getBlock(x, y, z);
		return isSolidBlock(block) || block.isReplaceable(world, x, y, z);
	}
	
	private boolean isSolidBlock(World world, int x, int y, int z) {
		return isSolidBlock(world.getBlock(x, y, z));
	}
	
	private boolean isSolidBlock(Block block) {
	    Material material =  block.getMaterial();
		return material.isSolid() && material.isOpaque();
	}

	private int getMaxPillarHeightAt(World world, int x, int y, int z) {
		int max = 0;

		for (Integer incX : CORNER_INC) {
			for (Integer incZ : CORNER_INC) {
				max = Math.max(max, getPillarHeightForCornerAt(world, x, y, z, incX, incZ));
			}
		}

		return max;
	}

	/**
	 * @param world The world.
	 * @param x Center x coordinate of the structure.
	 * @param y Height coordinate of the structure.
	 * @param z Center z coordinate of the structure.
	 * @param incX Indicates the corner X-direction.
	 * @param incZ Indicates the cordern Z-direction.
	 * @return Returns the height of a pillar in one of the corners, specified by (incX, incZ),
	 *         of the temple if it were centered at the given (x,y,z) location.
	 */
	private int getPillarHeightForCornerAt(World world, int x, int y, int z, int incX, int incZ) {
		int xx = x + 4*incX;
		int zz = z + 4*incZ;
		int res = 0;

		while (!isSolidBlock(world, xx, y, zz)) {
			y--;
			res++;
		}
		return res;
	}
	
	@Override
	protected void generateLayers() {
		BlockWrapper us = new BlockWrapper(Blocks.stone_slab, StairSlabMetadataHelper.getSlabMetadata(SlabType.STONE, true));	// upside down stone slab
		BlockWrapper rs = new BlockWrapper(Blocks.stone_slab);
		BlockWrapper ds = new BlockWrapper(Blocks.double_stone_slab);
		BlockWrapper cb = new BlockWrapper(Blocks.stonebrick, StairSlabMetadataHelper.getStoneBrickMetadata(StoneBrickType.CHISELED));	// chiseled brick
		BlockWrapper sb = new BlockWrapper(Blocks.stonebrick);
		BlockWrapper cs = new BlockWrapper(Blocks.stone_slab, StairSlabMetadataHelper.getSlabMetadata(SlabType.COBBLESTONE, false));	// cobblestone slab
		BlockWrapper co = new BlockWrapper(Blocks.cobblestone);
		BlockWrapper wa = new BlockWrapper(Blocks.water);
		BlockWrapper fe = new BlockWrapper(Blocks.fence);
		BlockWrapper to = new BlockWrapper(Blocks.torch);
		BlockWrapper cw = new BlockWrapper(Blocks.cobblestone_wall);
		
		BlockWrapper ea = new BlockWrapper(EnvironmentalAccumulator.getInstance());
		BlockWrapper o = null;	// Just to keep things compact...
		
		addLayer(1, new BlockWrapper[]{
				o ,o ,o ,o ,o ,o ,
				o ,o ,o ,us,ds,o ,
				us,us,us,us,us,o ,
				us,us,us,us,o ,o ,
				us,us,us,us,o ,o ,
				us,us,us,us,o ,o
			});
		
		addLayer(2, new BlockWrapper[]{
				o ,o ,o ,o ,o ,o ,
				o ,o ,o ,cb,cb,o ,
				sb,sb,sb,sb,cb,o ,
				ds,co,wa,sb,o ,o ,
				co,co,co,sb,o ,o ,
				co,co,ds,sb,o ,o
			});
		
		addLayer(3, new BlockWrapper[]{
				o ,o ,o ,o ,o ,o ,
				o ,o ,o ,o ,sb,o ,
				o ,o ,o ,fe,o ,o ,
				rs,o ,o ,o ,o ,o ,
				cs,rs,o ,o ,o ,o ,
				ea,cs,rs,o ,o ,o
			});
		
		addLayer(4, new BlockWrapper[]{
				o ,o ,o ,o ,o ,o ,
				o ,o ,o ,o ,cb,o ,
				o ,o ,o ,to,o ,o ,
				o ,o ,o ,o ,o ,o ,
				o ,o ,o ,o ,o ,o ,
				o ,o ,o ,o ,o ,o
			});
		
		addLayer(5, new BlockWrapper[]{
				us,o ,o ,o ,cw,o ,
				o ,o ,o ,o ,sb,cw,
				o ,o ,o ,o, o ,o ,
				o ,o ,o ,o ,o ,o ,
				o ,o ,o ,o ,o ,o ,
				o ,o ,o ,o ,o ,us
			});
		
		addLayer(6, new BlockWrapper[]{
				cb,ds,rs,rs,rs,o ,
				co,co,co,co,co,rs,
				co,co,co,co,co,rs,
				co,co,co,co,co,rs,
				co,co,co,co,co,ds,
				o ,co,co,co,co,cb
			});
		
		addLayer(7, new BlockWrapper[]{
				rs,o ,o ,o ,o ,o ,
				cw,o ,o ,o ,o ,o ,
				cs,cs,cs,o ,o ,o ,
				co,co,cs,cs,o ,o ,
				co,co,co,cs,o ,o ,
				o ,co,co,cs,cw,rs
			});
		
		addLayer(8, new BlockWrapper[]{
				o ,o ,o ,o ,o ,o ,
				o ,o ,o ,o ,o ,o ,
				o ,o ,o ,o ,o ,o ,
				o ,o ,o ,o ,o ,o ,
				cs,cw,o ,o ,o ,o ,
				o ,cs,o ,o ,o ,o
			});
		
		addLayer(9, new BlockWrapper[]{
				o ,o ,o ,o ,o ,o ,
				o ,o ,o ,o ,o ,o ,
				o ,o ,o ,o ,o ,o ,
				o ,o ,o ,o ,o ,o ,
				o ,to,o ,o ,o ,o ,
				o ,o ,o ,o ,o ,o
			});
	}
	
	@Override
	protected void postBuildCorner(World world, int x, int y, int z, int incX, int incZ) {
		// Place upside down stairs in corners
		
		// x+ east
		// z+ south
		// x- west
		// z- west
		int metadata1 = StairSlabMetadataHelper.getStairMetadata(DirectionHelpers.getForgeDirectionFromXSign(incX), true);	// metadata for stair 1
		int metadata2 = StairSlabMetadataHelper.getStairMetadata(DirectionHelpers.getForgeDirectionFromZSing(incZ), true);	// metadata for stair 2

		world.setBlock(x + 3*incX, y + 5, z + 4*incZ, Blocks.stone_stairs, metadata1, 2);
		world.setBlock(x + 4*incX, y + 5, z + 3*incZ, Blocks.stone_stairs, metadata2, 2);
		
		// pillars to the ground
		int xx = x + 4*incX;
		int zz = z + 4*incZ;
		int pillarHeight = getPillarHeightForCornerAt(world, x, y, z, incX, incZ);

		for (int yOffset = 0; yOffset < pillarHeight; yOffset++) {
			world.setBlock(xx, y - yOffset, zz, Blocks.cobblestone, 0, 2);
		}
	}
	
	@Override
    public boolean generate(World world, Random random, int x, int y, int z) {
		int groundHeight = findGround(world, x, z, getMinBuildHeight(), getMaxBuildHeight());
		
		while (groundHeight != -1) {
			// Check if we have room to place the structure here
			if (canPlaceStructure(world, x, groundHeight+1, z)) {
				// Only place the structure if the pillars are not too long
				if (getMaxPillarHeightAt(world, x, y, z) > GeneralConfig.darkTempleMaxPillarLength) return false;

				// If all is ok, generate the structure
				super.generate(world, random, x, groundHeight, z);

				// save position of the dark temple in NBT
				darkTempleData.addStructureLocation(x, groundHeight, z);

				return true;
			}
			
			groundHeight = findGround(world, x, z, getMinBuildHeight(), groundHeight-1);
		}
		
		return false;
	}
	
	private static int getMinBuildHeight() {
		return GeneralConfig.darkTempleMinHeight;
	}
	
	private static int getMaxBuildHeight() {
		return GeneralConfig.darkTempleMaxHeight - STRUCTURE_HEIGHT;
	}
	
	// Good seed for testing: -8353386167196066531
}
