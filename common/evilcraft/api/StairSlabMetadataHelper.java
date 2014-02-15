package evilcraft.api;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * A metadata helper for vanilla stairs and slabs.
 * @author immortaleeb
 *
 */
public class StairSlabMetadataHelper {
    
    /**
     * Get the metadata of a stair with a certain direction and orientation.
     * @param direction The direction the stair is facing.
     * @param upsideDown If the stair is upside down.
     * @return The meta data of the stair.
     */
	public static int getStairMetadata(ForgeDirection direction, boolean upsideDown) {
		int value;
		
		switch (direction) {
		    case WEST: value = 1;
		        break;
		    case SOUTH: value = 2;
		        break;
		    case NORTH: value = 3;
		        break;
		    default: value = 0;
		        break;
		}
		
		return (upsideDown) ? (4 | value) : value;
	}
	
	/**
	 * Type metadata for slab material types.
	 * @author immortaleeb
	 *
	 */
	@SuppressWarnings("javadoc")
	public enum SlabType {
		// Stone slabs
        STONE(0),
		SANDSTONE(1),
		WOODEN(2),
		COBBLESTONE(3),
		BRICK(4),
		STONE_BRICK(5),
		NETHER_BRICK(6),
		QUARTZ(7),
		
		// Wooden slabs
		OAK_WOOD(0),
		SPRUCE_WOOD(1),
		BIRCH_WOOD(2),
		JUNGLE_WOOD(3),
		ACACIA_WOOD(4),
		DARK_OAK_WOOD(5);
		
        /**
         * The metadata of that type.
         */
		public int metadata;
		
		private SlabType(int metadata) {
			this.metadata = metadata;
		}
	}
	
	/**
     * Get the metadata of a slab with a certain direction and orientation.
     * @param type The slab type.
     * @param upsideDown If the stair is upside down.
     * @return The meta data of the slab.
     */
	public static int getSlabMetadata(SlabType type, boolean upsideDown) {
		return upsideDown ? (8 | type.metadata) : type.metadata;
	}
	
	/**
     * Type metadata for brick material types.
     * @author immortaleeb
     *
     */
    @SuppressWarnings("javadoc")
	public enum StoneBrickType {
		REGULAR(0),
		MOSSY(1),
		CRACKED(2),
		CHISELED(3);
		
		public int metadata;
		
		private StoneBrickType(int metadata) {
			this.metadata = metadata;
		}
	}
	
    /**
     * Get the brick metadata of a given type.
     * @param type The brick type.
     * @return The brick metadata.
     */
	public static int getStoneBrickMetadata(StoneBrickType type) {
		return type.metadata;
	}
}
