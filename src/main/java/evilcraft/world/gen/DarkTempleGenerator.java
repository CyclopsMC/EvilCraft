package evilcraft.world.gen;

import com.google.common.collect.Maps;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import evilcraft.Configs;
import evilcraft.GeneralConfig;
import evilcraft.block.EnvironmentalAccumulatorConfig;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.world.gen.nbt.DarkTempleData;
import evilcraft.world.gen.structure.DarkTempleStructure;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

import java.util.Map;
import java.util.Random;

/**
 * World generator for Dark Temples.
 * @author immortaleeb
 * @author rubensworks
 *
 */
public class DarkTempleGenerator implements IWorldGenerator {

	private static Map<Integer, DarkTempleData> darkTemples = Maps.newHashMap();

	private static final String DARK_TEMPLE_MAP_NAME = "DarkTemple";

	public DarkTempleGenerator() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	public static DarkTempleData getCachedData(World world) {
		return darkTemples.get(world.provider.dimensionId);
	}

	private DarkTempleData loadData(World world) {
		DarkTempleData data = (DarkTempleData) world.perWorldStorage.loadData(DarkTempleData.class, DARK_TEMPLE_MAP_NAME);
		if(data == null) {
			data = new DarkTempleData(DARK_TEMPLE_MAP_NAME);
			world.perWorldStorage.setData(DARK_TEMPLE_MAP_NAME, data);
		}
		return data;
	}

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		if(!MinecraftHelpers.isClientSide()) {
			int id = event.world.provider.dimensionId;
			darkTemples.remove(id);
			DarkTempleData data = loadData(event.world);
			darkTemples.put(id, data);
		}
	}

	@SubscribeEvent
	public void onWorldSave(WorldEvent.Save event) {
		if(!MinecraftHelpers.isClientSide()) {
			DarkTempleData data = getCachedData(event.world);
			data.setDirty(true);
		}
	}

	protected boolean canGenerate(World world) {
		int id = world.provider.dimensionId;
		for(int i = 0; i < GeneralConfig.darkTempleDimensions.length; i++) {
			if(id == GeneralConfig.darkTempleDimensions[i]) {
				return true;
			}
		}
		return false;
	}

	protected boolean appliesAt(World world, int chunkX, int chunkZ) {
		int frequency = GeneralConfig.darkTempleFrequency;
		// Pseudo-random formula
		return (chunkX * chunkZ + chunkX - chunkZ + world.getSeed()) % frequency == 0;
	}

	@Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		if (canGenerate(world) && Configs.isEnabled(EnvironmentalAccumulatorConfig.class) && appliesAt(world, chunkX, chunkZ)) {
			int x = chunkX * 16 + random.nextInt(16);
			int y = 0;
			int z = chunkZ * 16 + random.nextInt(16);

			// Generate the dark temple if possible (height checks are performed inside generate)
			if(!DarkTempleStructure.getInstance().generate(world, random, x, y, z)) {
				getCachedData(world).addFailedLocation(chunkX, chunkZ);
			}
		}
    }
}
