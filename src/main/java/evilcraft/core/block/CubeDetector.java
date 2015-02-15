package evilcraft.core.block;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import evilcraft.api.ILocation;
import evilcraft.api.ISize;
import evilcraft.core.algorithm.Dimension;
import evilcraft.core.algorithm.Location;
import evilcraft.core.algorithm.Size;
import evilcraft.core.helper.LocationHelpers;
import net.minecraft.block.Block;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Detector of cubes in a world.
 * @author rubensworks
 *
 */
public class CubeDetector {
	
	private static Size NULL_SIZE = Size.NULL_SIZE.copy();
	
	private Collection<AllowedBlock> allowedBlocks = Sets.newHashSet();
	private Map<Block, AllowedBlock> blockInfo = Maps.newHashMap();
	private List<? extends IDetectionListener> listeners;
	private Size minimumSize = NULL_SIZE;
	
	private Map<Block, Integer> blockOccurences;

	/**
	 * Make a new instance.
	 * @param allowedBlocks The blocks that are allowed in this cube.
	 * @param listeners Listeners for detections. 
	 */
	public CubeDetector(AllowedBlock[] allowedBlocks, List<? extends IDetectionListener> listeners) {
		addAllowedBlocks(allowedBlocks);
		this.listeners = listeners;
	}
	
	/**
	 * @return the allowed blocks
	 */
	public Collection<AllowedBlock> getAllowedBlocks() {
		return allowedBlocks;
	}

	/**
	 * @param allowedBlocks The allowed blocks
	 */
	public void addAllowedBlocks(AllowedBlock[] allowedBlocks) {
		for(AllowedBlock block : allowedBlocks) {
			blockInfo.put(block.getBlock(), block);
			this.allowedBlocks.add(block);
		}
	}
	
	/**
	 * @return the minimumSize
	 */
	public Size getMinimumSize() {
		return minimumSize;
	}

	/**
	 * The size is the inner size -1 on each dimension, so 2x2x2 is one block in the middle open,
	 * with edge blocks on the side forming a 3x3x3.
	 * @param minimumSize the minimumSize to set
	 * @return this instance.
	 */
	public CubeDetector setMinimumSize(Size minimumSize) {
		this.minimumSize = minimumSize;
		return this;
	}
	
	/**
	 * @return the listeners
	 */
	public List<? extends IDetectionListener> getListeners() {
		return listeners;
	}
	
	protected void notifyListeners(World world, ILocation location, Size size, boolean valid) {
		for(IDetectionListener listener : getListeners()) {
			listener.onDetect(world, location, size, valid);
		}
	}
	
	protected boolean isValidLocation(World world, ILocation location, IValidationAction action) {
		Block block = world.getBlock(location.getCoordinates()[0], location.getCoordinates()[1],
				location.getCoordinates()[2]);
		boolean contains = blockInfo.containsKey(block);
        if(contains && action != null) action.onValidate(location, block);
        return contains;
	}

    protected boolean isValidLocation(World world, ILocation location) {
        return isValidLocation(world, location, null);
    }
	
	protected boolean isAir(World world, ILocation location) {
		return world.isAirBlock(location.getCoordinates()[0], location.getCoordinates()[1],
				location.getCoordinates()[2]);
	}
	
	/**
	 * Find the border of valid/non-valid locations in one given dimension for just one direction.
	 * @param world The world to look in.
	 * @param startLocation The location to start looking from.
	 * @param dimension The dimension to navigate in.
	 * @return The found border.
	 */
	protected ILocation navigateToBorder(World world, ILocation startLocation, int dimension, int direction) {
		ILocation loopLocation = startLocation.copy();
		
		// Loop until we find a non-valid location.
		while(isValidLocation(world, loopLocation)) {
			loopLocation.getCoordinates()[dimension] += direction;
		}
		
		// Because we went one increment too far.
		loopLocation.getCoordinates()[dimension] -= direction;
		
		return loopLocation;
	}
	
	/**
	 * Find the border of valid/non-valid locations in one given dimension (both directions).
	 * @param world The world to look in.
	 * @param startLocation The location to start looking from.
	 * @param dimension The dimension to navigate in.
	 * @param max If the direction to look in for the given dimension should be positive
	 * otherwise negative.
	 * @return The found border.
	 */
	protected ILocation navigateToBorder(World world, ILocation startLocation, int dimension, boolean max) {
		ILocation location = navigateToBorder(world, startLocation, dimension, max ? 1 : -1);
		return location;
	}
	
	/**
	 * Navigate to a corner from a given startlocation for the given dimensions.
	 * If you would want to navigate to a corner in a 3D world, you would only need 2 dimensions.
	 * @param world The world to look in.
	 * @param startLocation The location to start looking from.
	 * @param dimensions The dimension to navigate in.
	 * @param max If the maximum distance from the startLocation should be looked for.
	 * @return The corner location.
	 */
	protected ILocation navigateToCorner(World world, ILocation startLocation, int[] dimensions, boolean max) {
		ILocation navigateLocation = startLocation.copy();
		for(int dimension : dimensions) {
			navigateLocation = navigateToBorder(world, navigateLocation, dimension, max);
		}
		return navigateLocation;
	}
	
	protected boolean isEdge(World world, int[][] dimensionEgdes, ILocation location) {
		for (int i = 0; i < dimensionEgdes.length; i++) {
			for (int j = 0; j < dimensionEgdes[i].length; j++) {
				if(dimensionEgdes[i][j] == location.getCoordinates()[i]) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Check if a given location is valid, taking into account the edges of the structure, so that
	 * we know which locations should be borders, and which ones should be air.
	 * @param world The world.
	 * @param dimensionEgdes The edges per dimension. [dimension][start=0 | stop=1]
	 * @param location The location to check.
     * @param action The action to execute when a location has been validated.
	 * @return If the location was valid.
	 */
	protected boolean validateLocationInStructure(World world, int[][] dimensionEgdes, ILocation location,
                                                  IValidationAction action) {
		if (!isValidLocation(world, location, action)) {
			//System.out.println("No valid block at " + location);
			return false;
		}
		return true;
	}
	
	/**
	 * Run the {@link ILocationAction} for all the possible locations within this structure.
	 * @param world The world.
	 * @param dimensionEgdes The edges per dimension. [dimension][start=0 | stop=1]
	 * @param locationAction The runnable that will be called for each location in the structure.
	 * @return If the structure is valid for the given edges.
	 */
	protected boolean coordinateRecursion(World world, int[][] dimensionEgdes, ILocationAction locationAction) {
		return coordinateRecursion(world, dimensionEgdes, new int[]{}, locationAction);
	}
	
	/**
	 * Run the {@link ILocationAction} for all the possible locations within this structure.
	 * When the accumulatedCoordinates size equals the desired amount of dimensions,
	 * this recursion will enter a leaf and check the conditions for that location.
	 * @param world The world.
	 * @param dimensionEgdes The edges per dimension. [dimension][start=0 | stop=1]
	 * @param accumulatedCoordinates The accumulated coordinates up until now.
	 * @param locationAction The runnable that will be called for each location in the structure.
	 * @return If the structure is valid for the given edges.
	 */
	protected boolean coordinateRecursion(World world, int[][] dimensionEgdes, int[] accumulatedCoordinates,
                                          ILocationAction locationAction) {
		if(accumulatedCoordinates.length == dimensionEgdes.length) { // Leaf of recursion
			ILocation location = new Location(accumulatedCoordinates);
			if(!locationAction.run(world, location)) {
				return false;
			}
		} else { // Enter new recursion
			int dimension = accumulatedCoordinates.length;
			for(int i = dimensionEgdes[dimension][0]; i <= dimensionEgdes[dimension][1]; i++) {
				// Append the current dimension coordinate to the coordinate list.
				int[] newAccumulatedCoordinates = Arrays.copyOf(accumulatedCoordinates, accumulatedCoordinates.length + 1);
				newAccumulatedCoordinates[accumulatedCoordinates.length] = i;
				if(!coordinateRecursion(world, dimensionEgdes, newAccumulatedCoordinates, locationAction)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Validate if the block at the given location conforms with the {@link AllowedBlock}
	 * conditions.
	 * @param world The world.
	 * @param location The location.
	 * @return If it conforms.
	 */
	protected boolean validateAllowedBlockConditions(World world, ILocation location) {
		Block block = LocationHelpers.getBlock(world, location);
		if(blockInfo.containsKey(block)) {
			int occurences = blockOccurences.get(block);
			AllowedBlock allowed = blockInfo.get(block);
			
			if(allowed.getMaxOccurences() >= 0) {
				if(occurences >= allowed.getMaxOccurences()) {
					return false;
				}
			}
			
			blockOccurences.put(block, occurences + 1);
		}
		return true;
	}
	
	/**
	 * This will validate if the given structure has full borders and is hollow at the middle.
	 * To initiate the recursion, call this with an empty accumulatedCoordinates array, it will
	 * then simulate for loop for every dimension between the start and stop coordinate for that
	 * dimension. When the accumulatedCoordinates size equals the desired amount of dimensions,
	 * this recursion will enter a leaf and check the conditions for that location.
	 * @param world The world.
	 * @param dimensionEgdes The edges per dimension. [dimension][start=0 | stop=1]
	 * @param valid True if the structure should be validated, false if it should be invalidated.
     * @param action The action to execute when a location has been validated.
	 * @return If the structure is valid for the given edges.
	 */
	protected boolean validateDimensionEdges(World world, final int[][] dimensionEgdes,
			final boolean valid, final IValidationAction action) {
		// Init the block occurences counter on zero for all blocks.
		blockOccurences = Maps.newHashMap();
		for(AllowedBlock block : allowedBlocks) {
			blockOccurences.put(block.getBlock(), 0);
		}
		
		// Loop over all dimensions
		return coordinateRecursion(world, dimensionEgdes, new ILocationAction() {

			@Override
			public boolean run(World world, ILocation location) {
				// Only check the allowed block conditions if in validation mode,
				// normally this 'valid' check is not needed, but bugs are always possible...
				return !(valid && !validateAllowedBlockConditions(world, location)) &&
                        validateLocationInStructure(world, dimensionEgdes, location, action);
			}
			
		});
	}
	
	protected void postValidate(World world, final Size size, int[][] dimensionEgdes,
			final boolean valid) {
		coordinateRecursion(world, dimensionEgdes, new ILocationAction() {

			@Override
			public boolean run(World world, ILocation location) {
				notifyListeners(world, location, size, valid);
				return true;
			}
			
		});
	}

    /**
     * Detect a structure at the given start location.
     * @param world The world to look in.
     * @param startLocation The starting location.
     * @param valid True if the structure should be validated, false if it should be invalidated.
     * @param changeState If the post-validate actions should be called, and thus potentially change the world block
     *                    states.
     * @return The size of the found structure. Note that a size in a dimension
     * here starts counting from 0, so a 1x1x1 structure (=1 block) will return a
     * size of 0 in each dimension.
     */
    public Size detect(World world, ILocation startLocation, boolean valid, boolean changeState) {
        return detect(world, startLocation, valid, null, changeState);
    }
	
	/**
	 * Detect a structure at the given start location.
	 * @param world The world to look in.
	 * @param startLocation The starting location.
	 * @param valid True if the structure should be validated, false if it should be invalidated.
     * @param action The action to execute when a location has been validated.
     * @param changeState If the post-validate actions should be called, and thus potentially change the world block
     *                    states.
	 * @return The size of the found structure. Note that a size in a dimension
	 * here starts counting from 0, so a 1x1x1 structure (=1 block) will return a
	 * size of 0 in each dimension.
	 */
	public Size detect(World world, ILocation startLocation, boolean valid, IValidationAction action, boolean changeState) {
		// Next to the origin, we only need one corner for each dimension,
		// we can easily derive if the structure is valid with these 4 corners.
		
		// First detect if the given location is a valid block.
		if(!isValidLocation(world, startLocation)) {
			return NULL_SIZE.copy();
		}
		
		// Find a corner that can be used as an origin for the structure.
		// We use a temp origin corner to first go to the completely opposite direction
		// in each dimension to ensure we eventually find the actual origin.
		ILocation tempOriginCorner = navigateToCorner(world, startLocation, new int[]{2, 1, 0}, true);
		ILocation originCorner = navigateToCorner(world, tempOriginCorner, new int[]{0, 1, 2}, false);
		
		// Find corners in each dimension starting from the origin.
		ILocation[] corners = new ILocation[Dimension.DIMENSIONS.length];
		for(int i = 0; i < corners.length; i++) {
			corners[i] = navigateToCorner(world, originCorner, new int[]{i}, true);
		}
		
		// Measure the size of the cube with the found corners.
		// Also save each start and stop coordinate for each dimension,
		// we'll use this in the validation of the cube.
		int[] distances = new int[corners.length];
		int[][] dimensionEgdes = new int[corners.length][2]; // [dimension][start | stop]
		for(int i = 0; i < corners.length; i++) {
			// Distance measurement
			ISize sizeDifference = corners[i].getDifference(originCorner);
			distances[i] = sizeDifference.getCoordinates()[i];
			
			// Start and stop coordinate measurement.
			int addIndex = 0;
			if(originCorner.getCoordinates()[i] > corners[i].getCoordinates()[i]) {
				addIndex = 1;
			}
			dimensionEgdes[i][(0 + addIndex) % 2] = originCorner.getCoordinates()[i];
			dimensionEgdes[i][(1 + addIndex) % 2] = corners[i].getCoordinates()[i];
		}
		
		// Loop over each block of the cube and check if they have valid blocks or are air.
		if(!validateDimensionEdges(world, dimensionEgdes, valid, action)) {
			return NULL_SIZE.copy();
		}
		
		Size size = new Size(distances);
		// Check if the size is not smaller than the minimum required size.
		// If it is smaller we immediately return a null-size, but if we are in invalidation-mode,
		// we skip this step because we always want to be able to invalidate existing structures.
		// TODO: the 'valid' condition might be impossible to have influence if all structure
		// handling goes according to plan.
		if(size.compareTo(getMinimumSize()) < 0 && valid) {
			// System.out.println("too small");
			return NULL_SIZE.copy();
		}
		postValidate(world, size, dimensionEgdes, valid);
		return size;
	}
	
	/**
	 * Listener for detections.
	 * @author rubensworks
	 *
	 */
	public interface IDetectionListener {
		
		/**
		 * Called when a new structure has been detected.
		 * @param world The world.
		 * @param location The location of a block of the structure.
		 * @param size The size of the structure.
		 * @param valid True if the structure should be validated, false if it should be invalidated.
		 */
		public void onDetect(World world, ILocation location, Size size, boolean valid);
		
	}
	
	protected interface ILocationAction {
		
		/**
		 * An action for {@link CubeDetector#coordinateRecursion(World, int[][], ILocationAction)}.
		 * @param world The world.
		 * @param location The location.
		 * @return If the recursion should continue. If one is false, the full
		 * {@link CubeDetector#coordinateRecursion(World, int[][], ILocationAction)} will return false.
		 */
		public boolean run(World world, ILocation location);
		
	}

    public static interface IValidationAction {

        /**
         * An action to execute when a location has been validated.
         * @param location The location that was successfully validated.
         * @param block The block on that location.
         */
        public void onValidate(ILocation location, Block block);

    }
	
}
