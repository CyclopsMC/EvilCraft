package org.cyclops.evilcraft.core.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.List;

/**
 * Detector of hollow cubes in a world.
 * @author rubensworks
 *
 */
public class HollowCubeDetector extends CubeDetector {
	
	/**
	 * Make a new instance.
	 * @param allowedBlocks The blocks that are allowed in this cube.
	 * @param listeners Listeners for detections. 
	 */
	public HollowCubeDetector(AllowedBlock[] allowedBlocks, List<? extends IDetectionListener> listeners) {
		super(allowedBlocks, listeners);
	}
	
	@Override
	protected void postValidate(World world, final Vec3i size, final int[][] dimensionEgdes, final boolean valid, final BlockPos originCorner, final BlockPos excludeLocation) {
		coordinateRecursion(world, dimensionEgdes, new BlockPosAction() {

			@Override
			public boolean run(World world, BlockPos location) {
				if(isEdge(world, dimensionEgdes, location) && isValidLocation(world, location, excludeLocation)) {
					notifyListeners(world, location, size, valid, originCorner);
				}
				return true;
			}
			
		});
	}
	
	@Override
	protected boolean validateLocationInStructure(World world, int[][] dimensionEgdes, BlockPos location, IValidationAction action, BlockPos excludeLocation) {
		// Validate edge or air.
		if (isEdge(world, dimensionEgdes, location)) {
			if (!isValidLocation(world, location, action, excludeLocation)) {
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
