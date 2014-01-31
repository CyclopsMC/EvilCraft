package evilcraft.worldgen;

import java.util.Random;

import evilcraft.EvilCraft;
import evilcraft.api.config.ElementTypeCategory;
import evilcraft.api.config.configurable.ConfigurableProperty;
import evilcraft.blocks.DarkOreConfig;
import evilcraft.blocks.NetherfishSpawn;
import evilcraft.blocks.NetherfishSpawnConfig;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class NetherfishSpawnGenerator extends WorldGenMinableConfigurable{
    
    public NetherfishSpawnGenerator() {
        super(NetherfishSpawnConfig._instance.ID, 1, NetherfishSpawnConfig.veinsPerChunk, 1, 127);
    }
    
    public void loopGenerate(World world, Random rand, int chunkX, int chunkZ) {
        for(int k = 0; k < veinsPerChunk; k++){
            int firstBlockXCoord = chunkX + rand.nextInt(16);
            int firstBlockYCoord = startY + rand.nextInt(endY - startY);
            int firstBlockZCoord = chunkZ + rand.nextInt(16);
            
            int blockID = world.getBlockId(firstBlockXCoord, firstBlockYCoord, firstBlockZCoord);
            int meta = NetherfishSpawn.getInstance().getMetadataFromBlockID(blockID);
            if(meta != -1) {
                this.generate(world, rand, firstBlockXCoord, firstBlockYCoord, firstBlockZCoord);
                world.setBlockMetadataWithNotify(firstBlockXCoord, firstBlockYCoord, firstBlockZCoord, meta, 2);
            }
        }
    }
}
