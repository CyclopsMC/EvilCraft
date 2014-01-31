package evilcraft.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;
import evilcraft.GeneralConfig;
import evilcraft.blocks.DarkOreConfig;
import evilcraft.blocks.NetherfishSpawnConfig;
import evilcraft.entities.monster.NetherfishConfig;

public class EvilWorldGenerator implements IWorldGenerator {
    
    private WorldGenMinableConfigurable darkOres;
    private WorldGenMinableConfigurable extraSilverfish;
    private WorldGenMinableConfigurable netherfish;
    
    public EvilWorldGenerator() {
        darkOres = new WorldGenMinableConfigurable(DarkOreConfig._instance.ID, DarkOreConfig._instance.blocksPerVein, DarkOreConfig._instance.veinsPerChunk, DarkOreConfig._instance.startY, DarkOreConfig._instance.endY);
        extraSilverfish = new WorldGenMinableConfigurable(Block.silverfish.blockID, 8, GeneralConfig.silverfish_BlocksPerVein, GeneralConfig.silverfish_VeinsPerChunk, GeneralConfig.silverfish_StartY, GeneralConfig.silverfish_EndY);
        netherfish = new NetherfishSpawnGenerator();
    }
    
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        if(world.provider.dimensionId == 0) generateSurface(world, random, chunkX * 16, chunkZ * 16);
        if(world.provider.dimensionId == -1) generateNether(world, random, chunkX * 16, chunkZ * 16);
    }
    
    private void generateSurface(World world, Random rand, int chunkX, int chunkZ) {
        darkOres.loopGenerate(world, rand, chunkX, chunkZ);
        if(GeneralConfig.extraSilverfish)
            extraSilverfish.loopGenerate(world, rand, chunkX, chunkZ);
    }
    
    private void generateNether(World world, Random rand, int chunkX, int chunkZ) {
        if(NetherfishConfig._instance.isEnabled())
            netherfish.loopGenerate(world, rand, chunkX, chunkZ);
    }
}
