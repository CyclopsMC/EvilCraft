package evilcraft.world.gen;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;
import evilcraft.Configs;
import evilcraft.GeneralConfig;
import evilcraft.block.DarkOre;
import evilcraft.block.DarkOreConfig;
import evilcraft.block.NetherfishSpawnConfig;
import evilcraft.entity.monster.NetherfishConfig;

/**
 * Main world generator for this mod.
 * @author rubensworks
 *
 */
public class EvilWorldGenerator implements IWorldGenerator {
    
    private WorldGenMinableConfigurable darkOres = null;
    private WorldGenMinableConfigurable extraSilverfish = null;
    private WorldGenMinableConfigurable netherfish = null;
    
    /**
     * Make new instance.
     */
    public EvilWorldGenerator() {
		if(Configs.isEnabled(DarkOreConfig.class))
			darkOres = new WorldGenMinableConfigurable(DarkOre.getInstance(), DarkOreConfig.blocksPerVein, DarkOreConfig.veinsPerChunk, DarkOreConfig.startY, DarkOreConfig.endY);
        extraSilverfish = new WorldGenMinableConfigurable(Blocks.monster_egg, 8, GeneralConfig.silverfish_BlocksPerVein, GeneralConfig.silverfish_VeinsPerChunk, GeneralConfig.silverfish_StartY, GeneralConfig.silverfish_EndY);
        if(Configs.isEnabled(NetherfishSpawnConfig.class) && Configs.isEnabled(NetherfishConfig.class))
        	netherfish = new NetherfishSpawnGenerator();
    }
    
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        if(world.provider.dimensionId == 0) generateSurface(world, random, chunkX * 16, chunkZ * 16);
        if(world.provider.dimensionId == -1) generateNether(world, random, chunkX * 16, chunkZ * 16);
    }
    
    private void generateSurface(World world, Random rand, int chunkX, int chunkZ) {
        if(darkOres != null)
            darkOres.loopGenerate(world, rand, chunkX, chunkZ);
        if(extraSilverfish != null && GeneralConfig.extraSilverfish)
            extraSilverfish.loopGenerate(world, rand, chunkX, chunkZ);
    }
    
    private void generateNether(World world, Random rand, int chunkX, int chunkZ) {
        if(netherfish != null)
            netherfish.loopGenerate(world, rand, chunkX, chunkZ);
    }
}
