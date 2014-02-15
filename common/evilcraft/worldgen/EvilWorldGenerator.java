package evilcraft.worldgen;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;
import evilcraft.GeneralConfig;
import evilcraft.blocks.DarkOre;
import evilcraft.blocks.DarkOreConfig;
import evilcraft.entities.monster.NetherfishConfig;

/**
 * Main world generator for this mod.
 * @author rubensworks
 *
 */
public class EvilWorldGenerator implements IWorldGenerator {
    
    private WorldGenMinableConfigurable darkOres;
    private WorldGenMinableConfigurable extraSilverfish;
    private WorldGenMinableConfigurable netherfish;
    
    /**
     * Make new instance.
     */
    public EvilWorldGenerator() {
        darkOres = new WorldGenMinableConfigurable(DarkOre.getInstance(), DarkOreConfig.blocksPerVein, DarkOreConfig.veinsPerChunk, DarkOreConfig.startY, DarkOreConfig.endY);
        extraSilverfish = new WorldGenMinableConfigurable(Blocks.monster_egg, 8, GeneralConfig.silverfish_BlocksPerVein, GeneralConfig.silverfish_VeinsPerChunk, GeneralConfig.silverfish_StartY, GeneralConfig.silverfish_EndY);
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
