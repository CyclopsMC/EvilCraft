package evilcraft.worldgen;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import cpw.mods.fml.common.IWorldGenerator;
import evilcraft.blocks.DarkOre;
import evilcraft.blocks.DarkOreConfig;

public class EvilWorldGenerator implements IWorldGenerator {
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        if(world.provider.dimensionId == 0) generateSurface(world, random, chunkX * 16, chunkZ * 16);
    }
    
    private void generateSurface(World world, Random rand, int chunkX, int chunkZ) {
        for(int k = 0; k < DarkOreConfig._instance.veinsPerChunk; k++){
            int firstBlockXCoord = chunkX + rand.nextInt(16);
            int firstBlockYCoord = DarkOreConfig._instance.startY + rand.nextInt(DarkOreConfig._instance.endY - DarkOreConfig._instance.startY);
            int firstBlockZCoord = chunkZ + rand.nextInt(16);
            
            new WorldGenMinable(DarkOreConfig._instance.ID, DarkOreConfig._instance.blocksPerVein).generate(world, rand, firstBlockXCoord, firstBlockYCoord, firstBlockZCoord);
        }
    }
}
