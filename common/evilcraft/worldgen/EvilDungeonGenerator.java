package evilcraft.worldgen;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import cpw.mods.fml.common.IWorldGenerator;
import evilcraft.blocks.DarkOre;
import evilcraft.blocks.DarkOreConfig;

public class EvilDungeonGenerator implements IWorldGenerator {
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        if(world.provider.dimensionId == 0) generateSurface(world, random, chunkX * 16, chunkZ * 16);
    }
    
    private void generateSurface(World world, Random random, int chunkX, int chunkZ) {
        int x = chunkX + random.nextInt(16);
        int y = random.nextInt(60);
        int z = chunkZ + random.nextInt(16);
        
        (new EvilDungeonStructure()).generate(world, random, x, y, z);
    }
}
