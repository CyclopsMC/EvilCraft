package evilcraft.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;

/**
 * WorldGenerator for mineable blocks.
 * @author rubensworks
 *
 */
public class WorldGenMinableConfigurable extends WorldGenMinable{
    
    protected int blocksPerVein;
    protected int veinsPerChunk;
    protected int startY;
    protected int endY;
    protected Block block;
    
    /**
     * Make a new instance.
     * @param block block to spawn.
     * @param blocksPerVein Blocks per vein.
     * @param veinsPerChunk Veins per chunk.
     * @param startY Start coordinate for Y
     * @param endY End coordinate for Y.
     */
    public WorldGenMinableConfigurable(Block block, int blocksPerVein, int veinsPerChunk, int startY, int endY) {
        super(block, blocksPerVein, Blocks.stone);
        this.block = block;
        this.blocksPerVein = blocksPerVein;
        this.veinsPerChunk = veinsPerChunk;
        this.startY = startY;
        this.endY = endY;
    }
    
    /**
     * Make a new instance.
     * @param block block to spawn.
     * @param meta Metadata of the block to spawn.
     * @param blocksPerVein Blocks per vein.
     * @param veinsPerChunk Veins per chunk.
     * @param startY Start coordinate for Y
     * @param endY End coordinate for Y.
     */
    public WorldGenMinableConfigurable(Block block, int meta, int blocksPerVein, int veinsPerChunk, int startY, int endY) {
        super(block, meta, blocksPerVein, Blocks.stone);
        this.blocksPerVein = blocksPerVein;
        this.veinsPerChunk = veinsPerChunk;
        this.startY = startY;
        this.endY = endY;
    }
    
    /**
     * Generate the ores in a loop for the veins per chunk/
     * @param world The world.
     * @param rand Random object.
     * @param chunkX X chunk coordinate.
     * @param chunkZ Z chunk coordinate.
     */
    public void loopGenerate(World world, Random rand, int chunkX, int chunkZ) {
        for(int k = 0; k < veinsPerChunk; k++){
            int firstBlockXCoord = chunkX + rand.nextInt(16);
            int firstBlockYCoord = startY + rand.nextInt(endY - startY);
            int firstBlockZCoord = chunkZ + rand.nextInt(16);
            
            this.generate(world, rand, firstBlockXCoord, firstBlockYCoord, firstBlockZCoord);
        }
    }
}
