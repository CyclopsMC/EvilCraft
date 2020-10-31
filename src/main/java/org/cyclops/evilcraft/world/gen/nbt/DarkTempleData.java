package org.cyclops.evilcraft.world.gen.nbt;

import com.google.common.collect.Maps;
import net.minecraft.world.dimension.Dimension;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
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
	private Map<String, Set<Pair<Integer, Integer>>> failedLocations = Maps.newHashMap();

	/**
	 * Creates a new instance.
     * @param mod The mod.
	 */
	public DarkTempleData(ModBase mod) {
		super(mod);
	}

	@Override
	public void onStartedEvent(FMLServerStartedEvent event) {
		// Hacky thing to make sure that locations that are inserted WHILE the server is starting, are not lost.
		Map<String, Set<Pair<Integer, Integer>>> oldFailedLocations = Maps.newHashMap(failedLocations);
		super.onStartedEvent(event);
		failedLocations.putAll(oldFailedLocations);
	}

	/**
	 * Add a failed location of a dark temple.
	 * @param dimension The dimension
	 * @param chunkX Chunk X
	 * @param chunkZ Chunk Y
	 */
	public void addFailedLocation(Dimension dimension, int chunkX, int chunkZ) {
		CollectionHelpers.addToMapSet(failedLocations, dimension.getType().getRegistryName().toString(), Pair.of(chunkX, chunkZ));
	}

	public boolean isFailed(Dimension dimension, int chunkX, int chunkZ) {
		String id = dimension.getType().getRegistryName().toString();
		return failedLocations.containsKey(id) && failedLocations.get(id).contains(Pair.of(chunkX, chunkZ));
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