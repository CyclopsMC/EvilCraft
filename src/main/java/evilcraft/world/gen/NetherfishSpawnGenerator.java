package evilcraft.world.gen;

import evilcraft.block.NetherfishSpawn;
import evilcraft.block.NetherfishSpawnConfig;
import evilcraft.core.world.gen.WorldGenMinableExtended;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import java.util.Random;

/**
 * WorldGenerator for netherfish spawn blocks.
 * @author rubensworks
 *
 */
public class NetherfishSpawnGenerator extends WorldGenMinableExtended{
    
    /**
     * Make a new instance.
     */
    public NetherfishSpawnGenerator() {
        super(NetherfishSpawn.getInstance(), 1, NetherfishSpawnConfig.veinsPerChunk, 1, 127, Blocks.netherrack);
    }
    
    @Override
    public void loopGenerate(World world, Random rand, int chunkX, int chunkZ) {
        for(int k = 0; k < veinsPerChunk; k++){
            int firstBlockXCoord = chunkX + rand.nextInt(16);
            int firstBlockYCoord = startY + rand.nextInt(endY - startY);
            int firstBlockZCoord = chunkZ + rand.nextInt(16);
            
            Block block = world.getBlock(firstBlockXCoord, firstBlockYCoord, firstBlockZCoord);
            int meta = NetherfishSpawn.getInstance().getMetadataFromBlock(block);
            if(meta != -1) {
                world.setBlock(firstBlockXCoord, firstBlockYCoord, firstBlockZCoord, block, meta, 2);
            }
        }
    }
}
