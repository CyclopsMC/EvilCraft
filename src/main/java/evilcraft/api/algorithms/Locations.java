package evilcraft.api.algorithms;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Helper methods for locations.
 * @author rubensworks
 *
 */
public class Locations {
	
	private static void validateLocation(ILocation location) throws LocationException {
		if(location.getDimensions() != 3) {
			throw new LocationException("The location '" + location
					+ "' does not have exactly three coordinated.");
		}
	}

	/**
	 * Get the block from a location in a world.
	 * @param world The world.
	 * @param location The location.
	 * @return The block on that location.
	 * @throws LocationException If the given location does not have exactly three dimensions.
	 */
	public static Block getBlock(World world, ILocation location) throws LocationException {
		int[] c = location.getCoordinates();
		validateLocation(location);
		return world.getBlock(c[0], c[1], c[2]);
	}
	
	/**
	 * Get the tile entity from a location in a world.
	 * @param world The world.
	 * @param location The location.
	 * @return The tile entity on that location.
	 * @throws LocationException If the given location does not have exactly three dimensions.
	 */
	public static TileEntity getTile(World world, ILocation location) throws LocationException {
		int[] c = location.getCoordinates();
		validateLocation(location);
		return world.getTileEntity(c[0], c[1], c[2]);
	}
	
	/**
	 * Exceptions that can occur with location manipulation.
	 * @author rubensworks
	 *
	 */
	public static class LocationException extends RuntimeException {
		
		/**
		 * Make a new instance.
		 * @param message The message.
		 */
		public LocationException(String message) {
			super(message);
		}
		
	}
	
}
