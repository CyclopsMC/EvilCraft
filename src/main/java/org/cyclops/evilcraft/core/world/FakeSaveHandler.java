package org.cyclops.evilcraft.core.world;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

import java.io.File;
import java.util.UUID;

/**
 * A dummy implementation of {@link ISaveHandler}, use with caution.
 * @author rubensworks
 *
 */
public class FakeSaveHandler implements ISaveHandler {
	
	private static final UUID uuid = UUID.randomUUID();

	@Override
	public WorldInfo loadWorldInfo() {
		return null;
	}

	@Override
	public void checkSessionLock() {
		
	}

	@Override
	public IChunkLoader getChunkLoader(WorldProvider var1) {
		return null;
	}

	@Override
	public void saveWorldInfoWithPlayer(WorldInfo var1, NBTTagCompound var2) {
		
	}

	@Override
	public void saveWorldInfo(WorldInfo var1) {
		
	}

    @Override
	public IPlayerFileData getPlayerNBTManager() {
		return null;
	}

	@Override
	public void flush() {
	}

	@Override
	public File getWorldDirectory() {
		return null;
	}

	@Override
	public File getMapFileFromName(String var1) {
		return null;
	}

	@Override
	public TemplateManager getStructureTemplateManager() {
		return null;
	}
	
	/**
	 * Overrides the getUUID method for Cauldron instances.
	 * @return A UUID for the fake world.
	 */
	public UUID getUUID() {
		return uuid;
	}

}
