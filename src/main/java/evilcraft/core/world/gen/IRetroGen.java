package evilcraft.core.world.gen;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.chunk.Chunk;

import java.util.Random;

/**
 * World generator that support retro-generation.
 * @author rubensworks
 *
 */
public interface IRetroGen {

	/**
	 * Regenerate for a chunk.
	 * @param tag The retrogen info tag for which retrogen should start. This tag is read-only.
	 * @param chunk The chunk.
	 * @param random The random instance.
	 */
	public void retroGenerateChunk(NBTTagCompound tag, Chunk chunk, Random random);
	
	/**
	 * If retrogen for this instance should be started.
	 * The NBT compound tag is specific for retrogeneration and contains data that is stored
	 * in {@link IRetroGen#afterRetroGen(NBTTagCompound)}.
	 * @param tag The NBT tag. This tag is read-only.
	 * @param dimensionId The id of the dimension.
	 * @return If retrogen should start for this.
	 */
	public boolean shouldRetroGen(NBTTagCompound tag, int dimensionId);
	
	/**
	 * Called after retrogen for this instance is done.
	 * Info should be stored inside the given tag about the retrogen status, so that it can
	 * be re-used later in {@link IRetroGen#shouldRetroGen(NBTTagCompound, int)}.
	 * @param tag The NBT tag. This will be stored inside the chunk data.
	 */
	public void afterRetroGen(NBTTagCompound tag);
	
}
