package evilcraft.worldgen.structure;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import evilcraft.api.Helpers;
import evilcraft.api.StairSlabMetadataHelper;
import evilcraft.api.StairSlabMetadataHelper.SlabType;
import evilcraft.api.StairSlabMetadataHelper.StoneBrickType;
import evilcraft.blocks.EnvironmentalAccumulator;

/**
 * Structure which generates Dark Temples.
 * 
 * @author immortaleeb
 *
 */
public class DarkTempleStructure extends QuarterSymmetricalStructure {
    
	private static final int STRUCTURE_HEIGHT = 9;
	private static final int MAX_BUILD_HEIGHT = 256 - STRUCTURE_HEIGHT;
	private static final int MIN_BUILD_HEIGHT = 90;
	
	private static DarkTempleStructure _instance = null;
	
	/**
	 * Get the unique instance.
	 * @return Unique instance.
	 */
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
	    Material material =  world.getBlock(x, y, z).getMaterial();
		return material.isSolid() && material.isOpaque();
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
		int metadata1 = StairSlabMetadataHelper.getStairMetadata(Helpers.getForgeDirectionFromXSign(incX), true);	// metadata for stair 1
		int metadata2 = StairSlabMetadataHelper.getStairMetadata(Helpers.getForgeDirectionFromZSing(incZ), true);	// metadata for stair 2

		world.setBlock(x + 3*incX, y + 5, z + 4*incZ, Blocks.stone_stairs, metadata1, 2);
		world.setBlock(x + 4*incX, y + 5, z + 3*incZ, Blocks.stone_stairs, metadata2, 2);
		
		// pillars to the ground
		int xx = x + 4*incX;
		int zz = z + 4*incZ;
		
		while (!isSolidBlock(world, xx, y, zz)) {
			world.setBlock(xx, y, zz, Blocks.cobblestone, 0, 2);
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
