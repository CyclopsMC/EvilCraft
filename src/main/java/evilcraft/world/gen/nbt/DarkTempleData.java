package evilcraft.world.gen.nbt;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldSavedData;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

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
	 * @param blockPos Coordinate of the dark temple.
	 */
	public void addStructureLocation(BlockPos blockPos) {
		structureLocations.add(new Position(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
		setDirty(true);
	}
	
	/**
	 * Check if a dark temple is in the range of the given coordinates.
	 * NOTE: this function will ignores the Y-coordinate because those are
	 * not interesting for dark temple spawning positions.
	 * @param blockPos The coordinate of the given location.
	 * @param range The range in which we have to search for other dark temples
	 *              around the given location.
	 * @return true if there is a dark temple in the given range, otherwise false is returned.
	 */
	public boolean isStructureInRange(BlockPos blockPos, int range) {
		for (Position pos : structureLocations) {
			if (inRange(pos, blockPos, range))
				return true;
		}
		
		return false;
	}
	
	private boolean inRange(Position pos, BlockPos blockPos, int range) {
		return (pos.getX() - blockPos.getX()) * (pos.getX() - blockPos.getX()) + (pos.getZ() - blockPos.getZ()) * (pos.getZ() - blockPos.getZ()) <= range * range;
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
		// TODO: this can be rewritten using an BlockPos instance and using NBTTagList.
		ByteBuffer buffer = ByteBuffer.allocate(4 + structureLocations.size() * Position.SIZE_IN_BYTES);
		
		// Write the number of elements in the list
		buffer.putInt(structureLocations.size());
		
		// Write the individual elements
		for (Position pos : structureLocations) {
			pos.addToByteBuffer(buffer);
		}
		
		compound.setByteArray("locations", buffer.array());
	}
	
	private static class Position extends BlockPos {
		public static final int SIZE_IN_BYTES = 3 * 4;
		
		public Position(int x, int y, int z) {
			super(x, y, z);
		}
		
		public void addToByteBuffer(ByteBuffer buffer) {
			buffer.putInt(getX());
			buffer.putInt(getY());
			buffer.putInt(getZ());
		}
		
		public static Position fromByteBuffer(ByteBuffer buffer) {
			return new Position(buffer.getInt(), buffer.getInt(), buffer.getInt());
		}
	}
}