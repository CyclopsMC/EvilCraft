package evilcraft.world.gen;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;
import evilcraft.Configs;
import evilcraft.GeneralConfig;
import evilcraft.block.EnvironmentalAccumulatorConfig;
import evilcraft.world.gen.nbt.DarkTempleData;
import evilcraft.world.gen.structure.DarkTempleStructure;

/**
 * World generator for Dark Temples.
 * @author immortaleeb
 *
 */
public class DarkTempleGenerator implements IWorldGenerator {

	private static DarkTempleData darkTempleData = null;
	private static final String DARK_TEMPLE_MAP_NAME = "DarkTemple";

	@Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		if (world.provider.dimensionId != 0 || !Configs.isEnabled(EnvironmentalAccumulatorConfig.class))
			return;
		
		// Load the dark temple data
		loadDarkTempleData(world);

		// Add some randomness to spawning
		if (random.nextInt(GeneralConfig.darkTempleRarity) == 0) {
			int x = chunkX * 16 + random.nextInt(16);
			int y = 0;
			int z = chunkZ * 16 + random.nextInt(16);

			// Check if there is no dark temple in the neighbourhood
			if (!darkTempleData.isStructureInRange(x, y, z, GeneralConfig.darkTempleMinDistance)) {
				// Generate the dark temple if possible (height checks are performed inside generate)
			    DarkTempleStructure.getInstance(darkTempleData).generate(world, random, x, y, z);
			}
		}
    }

	private static void loadDarkTempleData(World world) {
		if (darkTempleData == null) {System.out.println("NULL");
			darkTempleData = (DarkTempleData) world.perWorldStorage.loadData(DarkTempleData.class, DARK_TEMPLE_MAP_NAME);

			if (darkTempleData == null) {System.out.println("NEW");
				darkTempleData = new DarkTempleData(DARK_TEMPLE_MAP_NAME);
				world.perWorldStorage.setData(DARK_TEMPLE_MAP_NAME, darkTempleData);
			}
		}
	}
	
	/**
	 * Get the dark temple data.
	 * @param world The world.
	 * @return The data.
	 */
	public static DarkTempleData getDarkTempleData(World world) {
		if(world.provider.dimensionId == 0) {
			loadDarkTempleData(world);
			return darkTempleData;
		}
		return null;
	}
}
