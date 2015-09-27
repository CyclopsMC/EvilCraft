package evilcraft.world.gen.nbt;

import com.google.common.collect.Sets;
import evilcraft.core.helper.MinecraftHelpers;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldSavedData;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Set;

/**
 * Responsible for saving all dark temple locations in NBT format.
 * 
 * @author immortaleeb
 * @author rubensworks
 *
 */
public class DarkTempleData extends WorldSavedData {
	private Set<Pair<Integer, Integer>> failedLocations;

	/**
	 * Creates a new instance.
	 * @param mapName NBT tag and .dat filename for dark temples. 
	 */
	public DarkTempleData(String mapName) {
		super(mapName);
		initStructureLocations();
	}
	
	private void initStructureLocations() {
		failedLocations = Sets.newHashSet();
	}
	
	/**
	 * Add a failed location of a dark temple.
	 * @param chunkX Chunk X
	 * @param chunkZ Chunk Y
	 */
	public void addFailedLocation(int chunkX, int chunkZ) {
		failedLocations.add(Pair.of(chunkX, chunkZ));
		setDirty(true);
	}

	public boolean isFailed(int chunkX, int chunkZ) {
		return failedLocations.contains(Pair.of(chunkX, chunkZ));
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		if (!compound.hasKey("failedLocations")) return;
		initStructureLocations();

		NBTTagList list = compound.getTagList("failedLocations", MinecraftHelpers.NBTTag_Types.NBTTagCompound.ordinal());
		for(int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound posTag = list.getCompoundTagAt(i);
			failedLocations.add(Pair.of(posTag.getInteger("x"), posTag.getInteger("z")));
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		NBTTagList list = new NBTTagList();
		for (Pair<Integer, Integer> pos : failedLocations) {
			NBTTagCompound posTag = new NBTTagCompound();
			posTag.setInteger("x", pos.getLeft());
			posTag.setInteger("z", pos.getRight());
			list.appendTag(posTag);
		}
		compound.setTag("failedLocations", list);
	}
}