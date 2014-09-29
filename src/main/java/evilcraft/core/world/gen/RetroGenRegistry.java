package evilcraft.core.world.gen;

import java.util.Random;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkDataEvent;

import org.apache.logging.log4j.Level;

import com.google.common.collect.Sets;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import evilcraft.EvilCraft;
import evilcraft.GeneralConfig;

/**
 * Registry for {@link IRetroGen} instances.
 * @author rubensworks
 */
public class RetroGenRegistry {
	
	private static final String NBT_TAG_RETROGENERATED = "EvilCraft-RetroGen";
	
	private static RetroGenRegistry _instance = null;

	private Set<IRetroGen> retroGeneratables = Sets.newHashSet();
	private Random random = new Random();
	
	private RetroGenRegistry() {
		
	}
	
	/**
	 * @return The unique instance.
	 */
	public static RetroGenRegistry getInstance() {
		if(_instance == null) {
			_instance = new RetroGenRegistry();
		}
		return _instance;
	}
	
	/**
	 * Add a new retro-generatable instance.
	 * @param retroGen The retrogen instance.
	 */
	public void registerRetroGen(IRetroGen retroGen) {
		retroGeneratables.add(retroGen);
	}
	
	/**
	 * Called when a chunk loads.
	 * @param event The chunk load event.
	 */
	@SubscribeEvent
    public void retroGenLoad(ChunkDataEvent.Load event) {
		if(GeneralConfig.retrogen && event.getData() != null) {
			NBTTagCompound tag = event.getData().getCompoundTag(NBT_TAG_RETROGENERATED);
			if(tag == null) {
				tag = new NBTTagCompound();
			}
			setChunkSeed(event.world, event.getChunk());
			
			boolean atLeastOneModified = false;
			for(IRetroGen retroGen : retroGeneratables) {
				if(retroGen.shouldRetroGen(tag, event.world.provider.dimensionId)) {
					retroGen.retroGenerateChunk(tag, event.getChunk(), random);
					EvilCraft.log("Retrogenerating chunk at " 
							+ event.getChunk().xPosition + ":" + event.getChunk().zPosition, Level.INFO);
					atLeastOneModified = true;
				}
			}
			
			if(atLeastOneModified) {
				event.getChunk().setChunkModified();
			}
		}
	}
	
	private void setChunkSeed(World world, Chunk chunk) {
		// Based on RWTema's DenseOres retrogen
        random.setSeed(world.getSeed());
        long xSeed = random.nextLong() >> 2 + 1L;
        long zSeed = random.nextLong() >> 2 + 1L;
        long chunkSeed = (xSeed * chunk.xPosition + zSeed * chunk.zPosition) ^ world.getSeed();
        random.setSeed(chunkSeed);
	}
	
	/**
	 * Called when a chunk saves.
	 * @param event The chunk save event.
	 */
	@SubscribeEvent
    public void retroGenSave(ChunkDataEvent.Save event) {
		if(GeneralConfig.retrogen && event.getData() != null) {
			NBTTagCompound tag = event.getData().getCompoundTag(NBT_TAG_RETROGENERATED);
			if(tag == null) {
				tag = new NBTTagCompound();
			}
			for(IRetroGen retroGen : retroGeneratables) {
				retroGen.afterRetroGen(tag);
			}
			event.getData().setTag(NBT_TAG_RETROGENERATED, tag);
		}
	}
	
}
