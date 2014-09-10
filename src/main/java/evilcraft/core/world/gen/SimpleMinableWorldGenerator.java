package evilcraft.core.world.gen;

import java.util.List;
import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;

/**
 * A world generator in which {@link WorldGenMinableExtended} instances can be added.
 * Retrogen support is also implicity registered for these instances.
 * @author rubensworks
 *
 */
public class SimpleMinableWorldGenerator implements IWorldGenerator {

	private List<WorldGenMinableExtended> worldGenerators;
	
	/**
	 * Make a new instance.
	 * @param worldGenerators The world generator to add.
	 */
	public SimpleMinableWorldGenerator(List<WorldGenMinableExtended> worldGenerators) {
		this.worldGenerators = worldGenerators;
		for(WorldGenMinableExtended gen : worldGenerators) {
			RetroGenRegistry.getInstance().registerRetroGen(gen);
		}
	}
	
	@Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        for(WorldGenMinableExtended worldGen : worldGenerators) {
        	if(worldGen.getDimensionId() == world.provider.dimensionId) {
        		worldGen.loopGenerate(world, random, chunkX * 16, chunkZ * 16);
        	}
        }
    }
	
}
