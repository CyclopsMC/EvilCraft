package org.cyclops.evilcraft.core.world;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.List;

/**
 * A dummy implementation of {@link IChunkProvider}.
 * @author rubensworks
 *
 */
public class FakeChunkProvider implements IChunkProvider {

	@Override
	public boolean chunkExists(int var1, int var2) {
		return false;
	}

	@Override
	public Chunk provideChunk(int var1, int var2) {
		return null;
	}

    @Override
    public Chunk provideChunk(BlockPos p_177459_1_) {
        return null;
    }

	@Override
	public void populate(IChunkProvider var1, int var2, int var3) {
		
	}

    @Override
    public boolean func_177460_a(IChunkProvider p_177460_1_, Chunk p_177460_2_, int p_177460_3_, int p_177460_4_) {
        return false;
    }

    @Override
	public boolean saveChunks(boolean var1, IProgressUpdate var2) {
		return true;
	}

	@Override
	public boolean unloadQueuedChunks() {
		return true;
	}

	@Override
	public boolean canSave() {
		return false;
	}

	@Override
	public String makeString() {
		return new String();
	}

    @Override
    public List getPossibleCreatures(EnumCreatureType p_177458_1_, BlockPos p_177458_2_) {
        return null;
    }

    @Override
    public BlockPos getStrongholdGen(World worldIn, String p_180513_2_, BlockPos p_180513_3_) {
        return null;
    }

	@Override
	public int getLoadedChunkCount() {
		return 0;
	}

    @Override
    public void recreateStructures(Chunk p_180514_1_, int p_180514_2_, int p_180514_3_) {

    }

	@Override
	public void saveExtraData() {
		
	}

}
