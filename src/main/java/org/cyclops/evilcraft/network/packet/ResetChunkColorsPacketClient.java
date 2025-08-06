package org.cyclops.evilcraft.network.packet;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

/**
 * @author rubensworks
 */
public class ResetChunkColorsPacketClient {

    public static void onChunkLoaded(Level world, int chunkX, int chunkZ) {
        ((ClientLevel) world).onChunkLoaded(new ChunkPos(chunkX, chunkZ));
    }

}
