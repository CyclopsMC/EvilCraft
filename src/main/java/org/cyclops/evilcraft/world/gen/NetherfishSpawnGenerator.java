package org.cyclops.evilcraft.world.gen;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.world.gen.WorldGenMinableExtended;
import org.cyclops.evilcraft.block.NetherfishSpawn;
import org.cyclops.evilcraft.block.NetherfishSpawnConfig;

import java.util.Random;

/**
 * WorldGenerator for netherfish spawn blocks.
 * @author rubensworks
 *
 */
public class NetherfishSpawnGenerator extends WorldGenMinableExtended {
    
    /**
     * Make a new instance.
     */
    public NetherfishSpawnGenerator() {
        super(NetherfishSpawn.getInstance().getDefaultState(), 1, NetherfishSpawnConfig.veinsPerChunk, 1, 127, Blocks.netherrack);
    }
    
    @Override
    public void loopGenerate(World world, Random rand, int chunkX, int chunkZ) {
        for(int k = 0; k < veinsPerChunk; k++){
            BlockPos blockPos = new BlockPos(
                    chunkX + rand.nextInt(16),
                    startY + rand.nextInt(endY - startY),
                    chunkZ + rand.nextInt(16));
            
            Block block = world.getBlockState(blockPos).getBlock();
            Block spawnBlock = NetherfishSpawn.getInstance();
            int meta = NetherfishSpawn.getInstance().getMetadataFromBlock(block);
            if(meta != -1) {
                world.setBlockState(
                        blockPos,
                        spawnBlock.getDefaultState().withProperty(NetherfishSpawn.FAKEMETA, meta),
                        MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
            }
        }
    }
}
