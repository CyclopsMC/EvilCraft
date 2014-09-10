package evilcraft.world.gen.nbt;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

/**
 * Responsible for saving all dark temple locations in NBT format.
 * 
 * @author immortaleeb
 *
 */
public class DarkTempleData extends WorldSavedData {
	// Locations of all the dark temples
	private List<Position> structureLocations;

	/**
	 * Creates a new instance.
	 * @param mapName NBT tag and .dat filename for dark temples. 
	 */
	public DarkTempleData(String mapName) {
		super(mapName);
		initStructureLocations();
	}
	
	private void initStructureLocations() {
		structureLocations = new ArrayList<Position>();
	}
	
	/**
	 * Add a location of a new dark temple.
	 * @param x X-coordinate of the dark temple.
	 * @param y Y-coordinate of the dark temple.
	 * @param z Z-coordinate of the dark temple.
	 */
	public void addStructureLocation(int x, int y, int z) {
		structureLocations.add(new Position(x, y, z));
		setDirty(true);
	}
	
	/**
	 * Check if a dark temple is in the range of the given coordinates.
	 * NOTE: this function will ignores the Y-coordinate because those are
	 * not interesting for dark temple spawning positions.
	 * @param x The X-coordinate of the given location.
	 * @param y The Y-coordinate of the given location.
	 * @param z The Z-coordinate of the given location.
	 * @param range The range in which we have to search for other dark temples
	 *              around the given location.
	 * @return true if there is a dark temple in the given range, otherwise false is returned.
	 */
	public boolean isStructureInRange(int x, int y, int z, int range) {
		for (Position pos : structureLocations) {
			if (inRange(pos, x, y, z, range))
				return true;
		}
		
		return false;
	}
	
	private boolean inRange(Position pos, int x, int y, int z, int range) {
		return (pos.x - x) * (pos.x - x) + (pos.z - z) * (pos.z - z) <= range * range;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		if (!compound.hasKey("locations")) return;
		
		ByteBuffer buffer = ByteBuffer.wrap(compound.getByteArray("locations"));
		initStructureLocations();
		
		// Read the number of elements
		int nelems = buffer.getInt();
		
		// Read the individual elements
		for (int i=0; i < nelems; ++i) {
			structureLocations.add(Position.fromByteBuffer(buffer));
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		// TODO: this can be rewritten using an ILocation instance and using NBTTagList.
		ByteBuffer buffer = ByteBuffer.allocate(4 + structureLocations.size() * Position.SIZE_IN_BYTES);
		
		// Write the number of elements in the list
		buffer.putInt(structureLocations.size());
		
		// Write the individual elements
		for (Position pos : structureLocations) {
			pos.addToByteBuffer(buffer);
		}
		
		compound.setByteArray("locations", buffer.array());
	}
	
	private static class Position {
		public static final int SIZE_IN_BYTES = 3 * 4;
		
		public int x, y, z;
		
		public Position(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		public void addToByteBuffer(ByteBuffer buffer) {
			buffer.putInt(x);
			buffer.putInt(y);
			buffer.putInt(z);
		}
		
		public static Position fromByteBuffer(ByteBuffer buffer) {
			return new Position(buffer.getInt(), buffer.getInt(), buffer.getInt());
		}
	}
}