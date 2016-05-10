package org.cyclops.evilcraft.world.gen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.GeneralConfig;
import org.cyclops.evilcraft.block.EnvironmentalAccumulatorConfig;
import org.cyclops.evilcraft.world.gen.structure.DarkTempleStructure;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * World generator for Dark Temples.
 * @author immortaleeb
 *
 */
public class DarkTempleGenerator implements IWorldGenerator {
	@Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if (canGenerate(world) && Configs.isEnabled(EnvironmentalAccumulatorConfig.class) && appliesAt(world, chunkX, chunkZ)) {
			int x = chunkX * 16 + random.nextInt(16);
			int y = 0;
			int z = chunkZ * 16 + random.nextInt(16);

			// Generate the dark temple if possible (height checks are performed inside generate)
			if(!DarkTempleStructure.getInstance().generate(world, random, new BlockPos(x, y, z))) {
				EvilCraft.darkTempleData.addFailedLocation(world.provider.getDimension(), chunkX, chunkZ);
			}
		}
    }

	public static boolean canGenerate(World world) {
		int id = world.provider.getDimension();
		for(int i = 0; i < GeneralConfig.darkTempleDimensions.length; i++) {
			if(id == GeneralConfig.darkTempleDimensions[i]) {
				return true;
			}
		}
		return false;
	}

	protected static boolean appliesAt(World world, int chunkX, int chunkZ) {
		if(world.getBiomeGenForCoords(new BlockPos(chunkX * WorldHelpers.CHUNK_SIZE, 0, chunkZ * WorldHelpers.CHUNK_SIZE))
				.getTempCategory() == BiomeGenBase.TempCategory.OCEAN) {
			return false;
		}
		int frequency = GeneralConfig.darkTempleFrequency;
		// Pseudo-random formula
		return (chunkX * chunkZ + chunkX - chunkZ + world.getSeed()) % frequency == 0;
	}

	/**
	 * Check if a temple exists at the given chunk.
	 * @param world The world.
	 * @param chunkX Chunk X
	 * @param chunkZ Chunk Y
	 * @return If a temple exists and has not failed (yet).
	 */
	public static boolean hasTemple(World world, int chunkX, int chunkZ) {
		return appliesAt(world, chunkX, chunkZ) &&
				!EvilCraft.darkTempleData.isFailed(world.provider.getDimension(), chunkX, chunkZ);
	}

	/**
	 * Get the closest temple chunk location.
	 * It will search for the temple in a spiral iteration around the center position so that closer temples will be
	 * returned first.
	 * @param world The world.
	 * @param chunkX Chunk X
	 * @param chunkZ Chunk Y
	 * @param radius The maximum chunk distance to look for. The higher this is, the longer this will take.
	 * @return The pair of closest chunk x and z coordinates.
	 */
	public static @Nullable
	Pair<Integer, Integer> getClosest(World world, int chunkX, int chunkZ, int radius) {
		int x = 0;
		int z = 0;
		int dx = 0;
		int dz = -1;
		for(int r = 0; r < radius * radius; r++) {
			if ((-r / 2 <= x) && (x <= r / 2) && (-r / 2 <= z) && (z <= r / 2)) {
				if (hasTemple(world, chunkX + x, chunkZ + z)) {
					return Pair.of(chunkX + x, chunkZ + z);
				}
			}
			if( (x == z) || ((x < 0) && (x == -z)) || ((x > 0) && (x == 1 - z))){
				int t = dx;
				dx = -dz;
				dz = t;
			}
			x += dx;
			z += dz;
		}
		return null;
	}

	/**
	 * Get the closest temple chunk location.
	 * @param world The world.
	 * @param chunkX Chunk X
	 * @param chunkZ Chunk Y
	 * @return The pair of closest chunk x and z coordinates.
	 */
	public static @Nullable Pair<Integer, Integer> getClosest(World world, int chunkX, int chunkZ) {
		return getClosest(world, chunkX, chunkZ, Math.min(500, GeneralConfig.darkTempleFrequency));
	}

	/**
	 * Get the closest temple chunk location.
	 * @param world The world.
	 * @param x X
	 * @param z Z
	 * @return The pair location closest chunk in world coordinates.
	 */
	public static @Nullable
	BlockPos getClosestForCoords(World world, int x, int z) {
		Pair<Integer, Integer> closest = getClosest(world, x / WorldHelpers.CHUNK_SIZE, z / WorldHelpers.CHUNK_SIZE);
		if(closest == null) {
			return null;
		}
		return new BlockPos(closest.getLeft() * WorldHelpers.CHUNK_SIZE, 0, closest.getRight() * WorldHelpers.CHUNK_SIZE);
	}
}
