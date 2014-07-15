package evilcraft.api.block;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import evilcraft.api.algorithms.ILocation;

/**
 * Detector of hollow cubes in a world.
 * @author rubensworks
 *
 */
public class HollowCubeDetector extends CubeDetector {
	
	/**
	 * Make a new instance.
	 * @param allowedBlocks The blocks that are allowed in this cube.
	 */
	public HollowCubeDetector(Block[] allowedBlocks) {
		super(allowedBlocks);
	}
	
	@Override
	protected boolean validateLocationInStructure(World world, int[][] dimensionEgdes, ILocation location) {
		// Validate edge or air.
		if (isEdge(world, dimensionEgdes, location)) {
			if (!isValidLocation(world, location)) {
				//System.out.println("No edge at " + location);
				return false;
			}
		} else {
			if (!isAir(world, location)) {
				//System.out.println("No air at " + location);
				return false;
			}
		}
		return true;
	}

}
