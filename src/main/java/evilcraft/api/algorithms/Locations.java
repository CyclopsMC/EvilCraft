package evilcraft.api.algorithms;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import evilcraft.api.Helpers;

/**
 * Helper methods for locations.
 * @author rubensworks
 *
 */
public class Locations {
	
	private static int[] validateLocation(ILocation location) throws LocationException {
		if(location == null || location.getDimensions() != 3) {
			throw new LocationException("The location '" + location
					+ "' does not have exactly three coordinated.");
		}
		return location.getCoordinates();
	}

	/**
	 * Get the block from a location in a world.
	 * @param world The world.
	 * @param location The location.
	 * @return The block on that location.
	 * @throws LocationException If the given location does not have exactly three dimensions.
	 */
	public static Block getBlock(World world, ILocation location) throws LocationException {
		int[] c = validateLocation(location);
		return world.getBlock(c[0], c[1], c[2]);
	}
	
	/**
	 * Get the block metadata from a location in a world.
	 * @param world The world.
	 * @param location The location.
	 * @return The block on that location.
	 * @throws LocationException If the given location does not have exactly three dimensions.
	 */
	public static int getBlockMeta(World world, ILocation location) throws LocationException {
		int[] c = validateLocation(location);
		return world.getBlockMetadata(c[0], c[1], c[2]);
	}
	
	/**
	 * Get the tile entity from a location in a world.
	 * @param world The world.
	 * @param location The location.
	 * @return The tile entity on that location.
	 * @throws LocationException If the given location does not have exactly three dimensions.
	 */
	public static TileEntity getTile(World world, ILocation location) throws LocationException {
		int[] c = validateLocation(location);
		return world.getTileEntity(c[0], c[1], c[2]);
	}
	
	/**
	 * Set the block metadata.
	 * @param world The world.
	 * @param location The location.
	 * @param meta The meta value to set.
	 * @param notifyFlag The flag to set. See the BLOCK_NOTIFY_* fields in {@link Helpers}
	 * for more info, these can be or-ed together.
	 * @throws LocationException If the given location does not have exactly three dimensions.
	 */
	public static void setBlockMetadata(World world, ILocation location, int meta,
			int notifyFlag) throws LocationException {
		int[] c = validateLocation(location);
		world.setBlockMetadataWithNotify(c[0], c[1], c[2], meta, notifyFlag);
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
