package evilcraft.world.gen;

import java.util.List;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.IWorldGenerator;
import evilcraft.Configs;
import evilcraft.GeneralConfig;
import evilcraft.block.DarkOre;
import evilcraft.block.DarkOreConfig;
import evilcraft.block.NetherfishSpawnConfig;
import evilcraft.core.world.gen.RetroGenRegistry;
import evilcraft.entity.monster.NetherfishConfig;

/**
 * Main world generator for this mod.
 * @author rubensworks
 *
 */
public class EvilWorldGenerator implements IWorldGenerator {
    
	private List<WorldGenMinableConfigurable> worldGenerators = Lists.newLinkedList();
    
    /**
     * Make new instance.
     */
    public EvilWorldGenerator() {
		if(Configs.isEnabled(DarkOreConfig.class) && DarkOreConfig.blocksPerVein > 0 && DarkOreConfig.veinsPerChunk > 0) {
			WorldGenMinableConfigurable darkOres = new WorldGenMinableConfigurable(DarkOre.getInstance(), DarkOreConfig.blocksPerVein, DarkOreConfig.veinsPerChunk, DarkOreConfig.startY, DarkOreConfig.endY, Blocks.stone, 0);
			RetroGenRegistry.getInstance().registerRetroGen(darkOres);
			worldGenerators.add(darkOres);
		}
		if(GeneralConfig.extraSilverfish && GeneralConfig.silverfish_BlocksPerVein > 0 && GeneralConfig.silverfish_VeinsPerChunk > 0) {
			WorldGenMinableConfigurable extraSilverfish = new WorldGenMinableConfigurable(Blocks.monster_egg, 8, GeneralConfig.silverfish_BlocksPerVein, GeneralConfig.silverfish_VeinsPerChunk, GeneralConfig.silverfish_StartY, GeneralConfig.silverfish_EndY, Blocks.stone, 0);
	        RetroGenRegistry.getInstance().registerRetroGen(extraSilverfish);
	        worldGenerators.add(extraSilverfish);
		}
        if(NetherfishSpawnConfig.veinsPerChunk > 0 && Configs.isEnabled(NetherfishSpawnConfig.class) && Configs.isEnabled(NetherfishConfig.class)) {
        	WorldGenMinableConfigurable netherfish = new NetherfishSpawnGenerator();
        	RetroGenRegistry.getInstance().registerRetroGen(netherfish);
        	worldGenerators.add(netherfish);
        }
    }
    
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        for(WorldGenMinableConfigurable worldGen : worldGenerators) {
        	if(worldGen.getDimensionId() == world.provider.dimensionId) {
        		worldGen.loopGenerate(world, random, chunkX * 16, chunkZ * 16);
        	}
        }
    }
}
