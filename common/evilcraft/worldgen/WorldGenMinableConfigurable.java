package evilcraft.worldgen;

import java.util.Random;

import evilcraft.blocks.DarkOreConfig;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class WorldGenMinableConfigurable extends WorldGenMinable{
    
    protected int blocksPerVein;
    protected int veinsPerChunk;
    protected int startY;
    protected int endY;
    
    public WorldGenMinableConfigurable(int id, int blocksPerVein, int veinsPerChunk, int startY, int endY) {
        super(id, blocksPerVein, Block.stone.blockID);
        this.blocksPerVein = blocksPerVein;
        this.veinsPerChunk = veinsPerChunk;
        this.startY = startY;
        this.endY = endY;
    }
    
    public WorldGenMinableConfigurable(int id, int meta, int blocksPerVein, int veinsPerChunk, int startY, int endY) {
        super(id, meta, blocksPerVein, Block.stone.blockID);
        this.blocksPerVein = blocksPerVein;
        this.veinsPerChunk = veinsPerChunk;
        this.startY = startY;
        this.endY = endY;
    }
    
    public void loopGenerate(World world, Random rand, int chunkX, int chunkZ) {
        for(int k = 0; k < veinsPerChunk; k++){
            int firstBlockXCoord = chunkX + rand.nextInt(16);
            int firstBlockYCoord = startY + rand.nextInt(endY - startY);
            int firstBlockZCoord = chunkZ + rand.nextInt(16);
            
            this.generate(world, rand, firstBlockXCoord, firstBlockYCoord, firstBlockZCoord);
        }
    }
}
