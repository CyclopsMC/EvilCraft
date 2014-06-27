package evilcraft.worldgen;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;
import evilcraft.Configs;
import evilcraft.GeneralConfig;
import evilcraft.blocks.EnvironmentalAccumulatorConfig;
import evilcraft.worldgen.structure.DarkTempleStructure;

/**
 * World generator for Dark Temples.
 * @author immortaleeb
 *
 */
public class DarkTempleGenerator implements IWorldGenerator {
	@Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		if (world.provider.dimensionId != 0 || !Configs.isEnabled(EnvironmentalAccumulatorConfig.class))
			return;
		
		// Add some randomness to spawning
		if (random.nextInt(GeneralConfig.darkTempleRarity) == 0) {
			// Generate the dark temple if possible (height checks are performed inside generate)
		    DarkTempleStructure.getInstance().generate(world, random, chunkX * 16 + random.nextInt(16), 0, chunkZ * 16 + random.nextInt(16));
		}
    }
}
