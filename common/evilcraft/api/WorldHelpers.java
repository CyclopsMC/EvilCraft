package evilcraft.api;

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
    
}
