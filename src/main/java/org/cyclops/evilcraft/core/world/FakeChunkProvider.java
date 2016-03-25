package org.cyclops.evilcraft.core.world;

import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

/**
 * A dummy implementation of {@link IChunkProvider}.
 * @author rubensworks
 *
 */
public class FakeChunkProvider implements IChunkProvider {

	@Override
	public Chunk getLoadedChunk(int x, int z) {
		return null;
	}

	@Override
	public Chunk provideChunk(int x, int z) {
		return null;
	}

	@Override
	public boolean unloadQueuedChunks() {
		return false;
	}

	@Override
	public String makeString() {
		return null;
	}
}
