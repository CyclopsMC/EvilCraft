package evilcraft.api.block;

import java.util.List;

import net.minecraft.world.World;
import evilcraft.api.algorithms.ILocation;
import evilcraft.api.algorithms.Size;

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
	protected void postValidate(World world, final Size size, final int[][] dimensionEgdes, final boolean valid) {
		coordinateRecursion(world, dimensionEgdes, new ILocationAction() {

			@Override
			public boolean run(World world, ILocation location) {
				if(isEdge(world, dimensionEgdes, location) && isValidLocation(world, location)) {
					notifyListeners(world, location, size, valid);
				}
				return true;
			}
			
		});
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
