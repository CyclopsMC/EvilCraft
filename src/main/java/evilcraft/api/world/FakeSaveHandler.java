package evilcraft.api.world;

import java.io.File;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

/**
 * A dummy implementation of {@link ISaveHandler}, use with caution.
 * @author rubensworks
 *
 */
public class FakeSaveHandler implements ISaveHandler {

	@Override
	public WorldInfo loadWorldInfo() {
		return null;
	}

	@Override
	public void checkSessionLock() throws MinecraftException {
		
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
	public IPlayerFileData getSaveHandler() {
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
	public String getWorldDirectoryName() {
		return null;
	}

}
