package evilcraft.worldgen;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;
import evilcraft.worldgen.structure.DarkTempleStructure;

/**
 * World generator for Dark Temples.
 * @author immortaleeb
 *
 */
public class DarkTempleGenerator implements IWorldGenerator {
	@Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		if (world.provider.dimensionId != 0)
			return;
		
		// Add some randomness for spawning, might be too low?
		int modX = random.nextInt(7) + 3;
		int modZ = random.nextInt(7) + 3;
		
		if (chunkX % modX == random.nextInt(modX) && chunkZ % modZ == random.nextInt(modZ))
		    DarkTempleStructure.getInstance().generate(world, random, chunkX * 16 + random.nextInt(16), 0, chunkZ * 16 + random.nextInt(16));
    }
}
