package evilcraft.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import evilcraft.api.Helpers;
import evilcraft.api.MetadataHelper;
import evilcraft.api.MetadataHelper.SlabType;
import evilcraft.api.MetadataHelper.StoneBrickType;

public class DarkTempleStructure extends QuarterSymmetricalStructure {
	// DEBUG: seed: -2622550573405024187, coords: -325 119 -1469 and -50 102 -1592
	
	private static final int STRUCTURE_HEIGHT = 9;
	private static final int MAX_BUILD_HEIGHT = 256 - STRUCTURE_HEIGHT;
	private static final int MIN_BUILD_HEIGHT = 90;
	
	private static DarkTempleStructure _instance = null;
	
	public static DarkTempleStructure getInstance() {
		if (_instance == null)
			_instance = new DarkTempleStructure();
		
		return _instance;
	}
	
	private DarkTempleStructure() {
		super(6, 6);
	}
	
	private int findGround(World world, int x, int z) {
		int height = MAX_BUILD_HEIGHT;
		
		while (height >= MIN_BUILD_HEIGHT && world.isAirBlock(x, height, z)) {
			height--;
		}
		
		if (!world.isAirBlock(x, height, z))
			return height;
		
		return -1;
	}
	
	private boolean canPlaceStructure(World world, int x, int y, int z) {
		for (int xr = x - 2; xr < x + 2; xr++) {
			for (int zr = z - 2; zr < z + 2; zr++) {
				if (!world.isAirBlock(xr, y, zr))
					return false;
			}
		}
		
		return true;
	}
	
	private boolean isSolidBlock(World world, int x, int y, int z) {
		return Block.isNormalCube(world.getBlockId(x, y, z));
	}
	
	@Override
	protected void generateLayers() {
		int us = getBlockIdWithMetadata(Block.stoneSingleSlab.blockID, MetadataHelper.getSlabMetadata(SlabType.STONE, true));	// upside down stone slab
		int rs = Block.stoneSingleSlab.blockID;
		int ds = Block.stoneDoubleSlab.blockID;
		int cb = getBlockIdWithMetadata(Block.stoneBrick.blockID, MetadataHelper.getStoneBrickMetadata(StoneBrickType.CHISELED));	// chiseled brick
		int sb = Block.stoneBrick.blockID;
		int cs = getBlockIdWithMetadata(Block.stoneSingleSlab.blockID, MetadataHelper.getSlabMetadata(SlabType.COBBLESTONE, false));	// cobblestone slab
		int co = Block.cobblestone.blockID;
		int wa = Block.waterStill.blockID;
		int fe = Block.fence.blockID;
		int to = Block.torchWood.blockID;
		int cw = Block.cobblestoneWall.blockID;
		
		addLayer(1, new int[]{
				0 ,0 ,0 ,0 ,0 ,0 ,
				0 ,0 ,0 ,us,ds,0 ,
				us,us,us,us,us,0 ,
				us,us,us,us,0 ,0 ,
				us,us,us,us,0 ,0 ,
				0 ,us,us,us,0 ,0
			});
		
		addLayer(2, new int[]{
				0 ,0 ,0 ,0 ,0 ,0 ,
				0 ,0 ,0 ,cb,cb,0 ,
				sb,sb,sb,sb,cb,0 ,
				ds,co,wa,sb,0 ,0 ,
				co,co,co,sb,0 ,0 ,
				0 ,co,ds,sb,0 ,0
			});
		
		addLayer(3, new int[]{
				0 ,0 ,0 ,0 ,0 ,0 ,
				0 ,0 ,0 ,0 ,sb,0 ,
				0 ,0 ,0 ,fe,0 ,0 ,
				rs,0 ,0 ,0 ,0 ,0 ,
				cs,rs,0 ,0 ,0 ,0 ,
				0 ,cs,rs,0 ,0 ,0
			});
		
		addLayer(4, new int[]{
				0 ,0 ,0 ,0 ,0 ,0 ,
				0 ,0 ,0 ,0 ,cb,0 ,
				0 ,0 ,0 ,to,0 ,0 ,
				0 ,0 ,0 ,0 ,0 ,0 ,
				0 ,0 ,0 ,0 ,0 ,0 ,
				0 ,0 ,0 ,0 ,0 ,0
			});
		
		addLayer(5, new int[]{
				us,0 ,0 ,0 ,cw,0 ,
				0 ,0 ,0 ,0 ,sb,cw,
				0 ,0 ,0 ,0, 0 ,0 ,
				0 ,0 ,0 ,0 ,0 ,0 ,
				0 ,0 ,0 ,0 ,0 ,0 ,
				0 ,0 ,0 ,0 ,0 ,us
			});
		
		addLayer(6, new int[]{
				cb,ds,rs,rs,rs,0 ,
				co,co,co,co,co,rs,
				co,co,co,co,co,rs,
				co,co,co,co,co,rs,
				co,co,co,co,co,ds,
				0 ,co,co,co,co,cb
			});
		
		addLayer(7, new int[]{
				rs,0 ,0 ,0 ,0 ,0 ,
				cw,0 ,0 ,0 ,0 ,0 ,
				cs,cs,cs,0 ,0 ,0 ,
				co,co,cs,cs,0 ,0 ,
				co,co,co,cs,0 ,0 ,
				0 ,co,co,cs,cw,rs
			});
		
		addLayer(8, new int[]{
				0 ,0 ,0 ,0 ,0 ,0 ,
				0 ,0 ,0 ,0 ,0 ,0 ,
				0 ,0 ,0 ,0 ,0 ,0 ,
				0 ,0 ,0 ,0 ,0 ,0 ,
				cs,cw,0 ,0 ,0 ,0 ,
				0 ,cs,0 ,0 ,0 ,0
			});
		
		addLayer(9, new int[]{
				0 ,0 ,0 ,0 ,0 ,0 ,
				0 ,0 ,0 ,0 ,0 ,0 ,
				0 ,0 ,0 ,0 ,0 ,0 ,
				0 ,0 ,0 ,0 ,0 ,0 ,
				0 ,to,0 ,0 ,0 ,0 ,
				0 ,0 ,0 ,0 ,0 ,0
			});
		
		/*
				0 ,0 ,0 ,0 ,0 ,0 ,
				0 ,0 ,0 ,0 ,0 ,0 ,
				0 ,0 ,0 ,0 ,0 ,0 ,
				0 ,0 ,0 ,0 ,0 ,0 ,
				0 ,0 ,0 ,0 ,0 ,0 ,
				0 ,0 ,0 ,0 ,0 ,0
		 */
	}
	
	@Override
	protected void postBuildCorner(World world, int x, int y, int z, int incX, int incZ) {
		// Place upside down stairs in corners
		
		// x+ east
		// z+ south
		// x- west
		// z- west
		int metadata1 = MetadataHelper.getStairMetadata(Helpers.getForgeDirectionFromXSign(incX), true);	// metadata for stair 1
		int metadata2 = MetadataHelper.getStairMetadata(Helpers.getForgeDirectionFromZSing(incZ), true);	// metadata for stair 2

		world.setBlock(x + 3*incX, y + 5, z + 4*incZ, Block.stairsCobblestone.blockID, metadata1, 2);
		world.setBlock(x + 4*incX, y + 5, z + 3*incZ, Block.stairsCobblestone.blockID, metadata2, 2);
		
		// pillars to the ground
		int xx = x + 4*incX;
		int zz = z + 4*incZ;
		
		while (!isSolidBlock(world, xx, y, zz)) {
			world.setBlock(xx, y, zz, Block.cobblestone.blockID, 0, 2);
			y--;
		}
	}
	
	@Override
    public boolean generate(World world, Random random, int x, int y, int z) {
		int groundHeight = findGround(world, x, z);
		
		if (groundHeight == -1)
			return false;
		
		// Check if it is a valid spot
		if (!canPlaceStructure(world, x, groundHeight+1, z))
			return false;
		
		// It's a valid spot, now spawn it
		super.generate(world, random, x, groundHeight, z);
		
		return true;
	}
}
