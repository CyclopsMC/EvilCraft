package evilcraft.worldgen;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;

public class DarkTempleGenerator implements IWorldGenerator {
	@Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		if (world.provider.dimensionId != 0)
			return;
		
		// TODO: increase randomness of spawning temple structures
		
		DarkTempleStructure.getInstance().generate(world, random, chunkX * 16 + random.nextInt(16), 0, chunkZ * 16 + random.nextInt(16));
    }
}
