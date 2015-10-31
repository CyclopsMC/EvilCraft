package org.cyclops.evilcraft.world.gen.nbt;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.helper.CollectionHelpers;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.persist.nbt.NBTPersist;
import org.cyclops.cyclopscore.persist.world.WorldStorage;

import java.util.Map;
import java.util.Set;

/**
 * Responsible for saving all dark temple locations in NBT format.
 * 
 * @author immortaleeb
 * @author rubensworks
 *
 */
public class DarkTempleData extends WorldStorage {

	@NBTPersist
	private Map<Integer, Set<Pair<Integer, Integer>>> failedLocations = Maps.newHashMap();

	/**
	 * Creates a new instance.
     * @param mod The mod.
	 */
	public DarkTempleData(ModBase mod) {
		super(mod);
	}

	/**
	 * Add a failed location of a dark temple.
	 * @param dimensionId The dimension id
	 * @param chunkX Chunk X
	 * @param chunkZ Chunk Y
	 */
	public void addFailedLocation(int dimensionId, int chunkX, int chunkZ) {
		CollectionHelpers.addToMapSet(failedLocations, dimensionId, Pair.of(chunkX, chunkZ));
	}

	public boolean isFailed(int dimensionId, int chunkX, int chunkZ) {
		return failedLocations.containsKey(dimensionId) && failedLocations.get(dimensionId).contains(Pair.of(chunkX, chunkZ));
	}

    @Override
    public void reset() {
		failedLocations = Maps.newHashMap();
    }

    @Override
    protected String getDataId() {
        return "DarkTemple";
    }
}