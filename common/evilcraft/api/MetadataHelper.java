package evilcraft.api;

import net.minecraftforge.common.ForgeDirection;

public class MetadataHelper {
	public static int getStairMetadata(ForgeDirection direction, boolean upsideDown) {
		int value = 0;
		
		switch (direction) {
		case WEST: value = 1; break;
		case SOUTH: value = 2; break;
		case NORTH: value = 3;break;
		}
		
		return (upsideDown) ? (4 | value) : value;
	}
	
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
		
		public int metadata;
		
		private SlabType(int metadata) {
			this.metadata = metadata;
		}
	}
	
	public static int getSlabMetadata(SlabType type, boolean upsideDown) {
		return upsideDown ? (8 | type.metadata) : type.metadata;
	}
	
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
	
	public static int getStoneBrickMetadata(StoneBrickType type) {
		return type.metadata;
	}
}
