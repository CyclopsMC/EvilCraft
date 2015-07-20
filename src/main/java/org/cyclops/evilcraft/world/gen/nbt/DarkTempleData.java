package org.cyclops.evilcraft.world.gen.nbt;

import com.google.common.collect.Lists;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.persist.nbt.NBTPersist;
import org.cyclops.cyclopscore.persist.world.WorldStorage;

import java.util.List;

/**
 * Responsible for saving all dark temple locations in NBT format.
 * 
 * @author immortaleeb
 *
 */
public class DarkTempleData extends WorldStorage {

	@NBTPersist
	private List<Vec3i> structureLocations = Lists.newLinkedList();

	/**
	 * Creates a new instance.
     * @param mod The mod.
	 */
	public DarkTempleData(ModBase mod) {
		super(mod);
	}
	
	/**
	 * Add a location of a new dark temple.
	 * @param blockPos Coordinate of the dark temple.
	 */
	public void addStructureLocation(BlockPos blockPos) {
		structureLocations.add(blockPos);
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
		for (Vec3i pos : structureLocations) {
			if (inRange(pos, blockPos, range))
				return true;
		}
		
		return false;
	}
	
	private boolean inRange(Vec3i pos, BlockPos blockPos, int range) {
		return (pos.getX() - blockPos.getX()) * (pos.getX() - blockPos.getX()) + (pos.getZ() - blockPos.getZ()) * (pos.getZ() - blockPos.getZ()) <= range * range;
	}

    @Override
    public void reset() {
        structureLocations = Lists.newLinkedList();
    }

    @Override
    protected String getDataId() {
        return "DarkTemple";
    }
}