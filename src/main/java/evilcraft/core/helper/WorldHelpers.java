package evilcraft.core.helper;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

/**
 * Helpers for world related logic.
 * @author rubensworks
 *
 */
public class WorldHelpers {
   
    private static final int CHUNK_SIZE = 16;
    
    private static final double TICK_LAG_REDUCTION_MODULUS_MODIFIER = 1.0D;

    /**
     * Set the biome for the given coordinates.
     * CAN ONLY BE CALLED ON SERVERS!
     * @param world The world.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param biome The biome to change to.
     */
    //@SideOnly(Side.SERVER)
    @SuppressWarnings("unchecked")
    public static void setBiome(World world, int x, int z, BiomeGenBase biome) {
        Chunk chunk = world.getChunkFromBlockCoords(x, z);
        if(chunk != null) {
            int rx = x & (CHUNK_SIZE - 1);
            int rz = z & (CHUNK_SIZE - 1);
            byte[] biomeArray = chunk.getBiomeArray();
            biomeArray[rz << 4 | rx] = (byte)(biome.biomeID & 255);
            chunk.setChunkModified();
            world.getChunkProvider().loadChunk(chunk.xPosition, chunk.zPosition);
            
            // Notify the players for a chunk update.
            for(EntityPlayerMP player : (List<EntityPlayerMP>) MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
                List<ChunkCoordIntPair> chunks = (List<ChunkCoordIntPair>)player.loadedChunks;
                chunks.add(new ChunkCoordIntPair(chunk.xPosition, chunk.zPosition));
            }
        }
    }

	/**
	 * Check if an efficient tick can happen.
	 * This is useful for opererations that should happen frequently, but not strictly every tick.
	 * @param world The world to tick in.
	 * @param baseModulus The amount of ticks that could be skipped.
	 * @param params Optional parameters to further vary the tick occurences.
	 * @return If a tick of some operation can occur.
	 */
	public static boolean efficientTick(World world, int baseModulus, int... params) {
		int mod = (int) (baseModulus * TICK_LAG_REDUCTION_MODULUS_MODIFIER);
		if(mod == 0) mod = 1;
		int offset = 0;
		for(int param : params) offset += param;
		return world.rand.nextInt(mod) == offset % mod;
	}
    
}
