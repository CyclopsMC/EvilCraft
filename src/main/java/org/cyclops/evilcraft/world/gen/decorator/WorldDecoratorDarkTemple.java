package org.cyclops.evilcraft.world.gen.decorator;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.server.ServerChunkProvider;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.GeneralConfig;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * World generator for Dark Temples.
 * @author immortaleeb
 *
 */
public class WorldDecoratorDarkTemple extends Placement<ChanceConfig> {

	public WorldDecoratorDarkTemple(Function<Dynamic<?>, ? extends ChanceConfig> config) {
		super(config);
	}

	@Override
	public Stream<BlockPos> getPositions(IWorld world, ChunkGenerator<? extends GenerationSettings> generatorIn, Random random, ChanceConfig configIn, BlockPos pos) {
		if (canGenerate(world) && appliesAt(world, pos)) {
			if (isTooClose(world, pos)) {
				EvilCraft.darkTempleData.addFailedLocation(world.getDimension(), pos.getX() / WorldHelpers.CHUNK_SIZE, pos.getZ() / WorldHelpers.CHUNK_SIZE);
				return Stream.empty();
			}
			int i = configIn.chance;
			return IntStream.range(0, i).mapToObj((p_227448_3_) -> {
				int j = random.nextInt(16) + 8 + pos.getX();
				int k = random.nextInt(16) + 8 + pos.getZ();
				int l = random.nextInt(generatorIn.getMaxHeight());
				return new BlockPos(j, l, k);
			});
		}
		return Stream.empty();
	}

	private static boolean isTooClose(IWorld world, BlockPos pos) {
		return getClosest(world, pos.getX() / WorldHelpers.CHUNK_SIZE, pos.getZ() / WorldHelpers.CHUNK_SIZE, GeneralConfig.darkTempleMinimumChunkDistance, false, true) != null;
	}

	public static boolean canGenerate(IWorld world) {
		String id = world.getDimension().getType().getRegistryName().toString();
		return GeneralConfig.darkTempleDimensions.contains(id);
	}

	protected static boolean appliesAt(IWorld world, BlockPos pos) {
		if(world.getBiome(pos).getTempCategory() == Biome.TempCategory.OCEAN) {
			return false;
		}
		int frequency = GeneralConfig.darkTempleFrequency;
		// Pseudo-random formula
		return (pos.getX() * pos.getZ() + pos.getX() - pos.getZ() + world.getSeed()) % frequency == 0;
	}

	/**
	 * Check if a temple exists at the given chunk.
	 * @param world The world.
	 * @param chunkX Chunk X
	 * @param chunkZ Chunk Y
	 * @return If a temple exists and has not failed (yet).
	 */
	public static boolean hasTemple(IWorld world, int chunkX, int chunkZ) {
		return appliesAt(world, new BlockPos(chunkX * WorldHelpers.CHUNK_SIZE, 0, chunkZ * WorldHelpers.CHUNK_SIZE)) &&
				!EvilCraft.darkTempleData.isFailed(world.getDimension(), chunkX, chunkZ);
	}

	/**
	 * Get the closest temple chunk location.
	 * It will search for the temple in a spiral iteration around the center position so that closer temples will be
	 * returned first.
	 * @param world The world.
	 * @param chunkX Chunk X
	 * @param chunkZ Chunk Y
	 * @param radius The maximum chunk distance to look for. The higher this is, the longer this will take.
	 * @param includeOrigin If the given chunk coordinates should be checked when looking for closest.
	 * @param skipNonGeneratedChunks If chunks that are not generated yet should be skipped.
	 * @return The pair of closest chunk x and z coordinates.
	 */
	public static @Nullable
	Pair<Integer, Integer> getClosest(IWorld world, int chunkX, int chunkZ, int radius, boolean includeOrigin, boolean skipNonGeneratedChunks) {
		int x = 0;
		int z = 0;
		int dx = 0;
		int dz = -1;
		int maxSteps = (int) Math.pow(radius, 2);
		for(int r = 0; r < maxSteps; r++) {
			if ((-r / 2 <= x) && (x <= r / 2) && (-r / 2 <= z) && (z <= r / 2)) {
				if ((includeOrigin || (x != 0 || z != 0))
						&& (!skipNonGeneratedChunks || ((ServerChunkProvider) world.getChunkProvider()).chunkExists(chunkX + x, chunkZ + z))
						&& hasTemple(world, chunkX + x, chunkZ + z)) {
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
	@Nullable
	public static Pair<Integer, Integer> getClosest(IWorld world, int chunkX, int chunkZ) {
		return getClosest(world, chunkX, chunkZ, Math.min(500, GeneralConfig.darkTempleFrequency), true, false);
	}

	/**
	 * Get the closest temple chunk location.
	 * @param world The world.
	 * @param x X
	 * @param z Z
	 * @return The pair location closest chunk in world coordinates.
	 */
	@Nullable
	public static BlockPos getClosestForCoords(IWorld world, int x, int z) {
		Pair<Integer, Integer> closest = getClosest(world, x / WorldHelpers.CHUNK_SIZE, z / WorldHelpers.CHUNK_SIZE);
		if(closest == null) {
			return null;
		}
		return new BlockPos(closest.getLeft() * WorldHelpers.CHUNK_SIZE, 0, closest.getRight() * WorldHelpers.CHUNK_SIZE);
	}
}
