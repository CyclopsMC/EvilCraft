package evilcraft.core.world.gen;

import evilcraft.core.helper.WorldHelpers;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.feature.WorldGenMinable;

import java.util.Random;

/**
 * WorldGenerator for mineable blocks.
 * @author rubensworks
 *
 */
public class WorldGenMinableExtended extends WorldGenMinable implements IRetroGen {
    
	private static final int NO_META = 0;
	
    protected int blocksPerVein;
    protected int veinsPerChunk;
    protected int startY;
    protected int endY;
    protected Block block;
    protected Block replaceTarget;
    protected int meta = NO_META;
    protected int dimensionId;
    
    /**
     * Make a new instance.
     * @param block block to spawn.
     * @param blocksPerVein Blocks per vein.
     * @param veinsPerChunk Veins per chunk.
     * @param startY Start coordinate for Y
     * @param endY End coordinate for Y.
     * @param replaceTarget The replace target block. Stone for overworld, netherrack for nether.
     * @param dimensionId The dimension Id;
     */
    public WorldGenMinableExtended(Block block, int blocksPerVein, int veinsPerChunk, int startY, int endY, Block replaceTarget, int dimensionId) {
        super(block, blocksPerVein, replaceTarget);
        this.block = block;
        this.blocksPerVein = blocksPerVein;
        this.veinsPerChunk = veinsPerChunk;
        this.startY = startY;
        this.endY = endY;
        this.replaceTarget = replaceTarget;
        this.dimensionId = dimensionId;
    }
    
    /**
     * Make a new instance.
     * @param block block to spawn.
     * @param meta Metadata of the block to spawn.
     * @param blocksPerVein Blocks per vein.
     * @param veinsPerChunk Veins per chunk.
     * @param startY Start coordinate for Y
     * @param endY End coordinate for Y.
     * @param replaceTarget The replace target block. Stone for overworld, netherrack for nether.
     * @param dimensionId The dimension Id;
     */
    public WorldGenMinableExtended(Block block, int meta, int blocksPerVein, int veinsPerChunk, int startY, int endY, Block replaceTarget, int dimensionId) {
        super(block, meta, blocksPerVein, Blocks.stone);
        this.block = block;
        this.blocksPerVein = blocksPerVein;
        this.veinsPerChunk = veinsPerChunk;
        this.startY = startY;
        this.endY = endY;
        this.meta = meta;
        this.dimensionId = dimensionId;
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
    
    protected String getUniqueName() {
    	return block.getUnlocalizedName();
    }
    
    /**
     * Generate for only one chunk (this will NOT cross chunk boundaries!)
     * Based on the generate method for World instead of Chunk.
     * @param chunk The chunk to generate in.
     * @param rand Random.
     * @param x Chunk X.
     * @param y Chunk Y.
     * @param z Chunk Z.
     * @return If generation succeeded.
     */
    protected boolean generate(Chunk chunk, Random rand, int x, int y, int z) {
        float f = rand.nextFloat() * (float)Math.PI;
        double d0 = (double)((float)(x + 8) + MathHelper.sin(f) * (float)this.blocksPerVein / 8.0F);
        double d1 = (double)((float)(x + 8) - MathHelper.sin(f) * (float)this.blocksPerVein / 8.0F);
        double d2 = (double)((float)(z + 8) + MathHelper.cos(f) * (float)this.blocksPerVein / 8.0F);
        double d3 = (double)((float)(z + 8) - MathHelper.cos(f) * (float)this.blocksPerVein / 8.0F);
        double d4 = (double)(y + rand.nextInt(3) - 2);
        double d5 = (double)(y + rand.nextInt(3) - 2);

        for (int l = 0; l <= this.blocksPerVein; ++l) {
            double d6 = d0 + (d1 - d0) * (double)l / (double)this.blocksPerVein;
            double d7 = d4 + (d5 - d4) * (double)l / (double)this.blocksPerVein;
            double d8 = d2 + (d3 - d2) * (double)l / (double)this.blocksPerVein;
            double d9 = rand.nextDouble() * (double)this.blocksPerVein / 16.0D;
            double d10 = (double)(MathHelper.sin((float)l * (float)Math.PI / (float)this.blocksPerVein) + 1.0F) * d9 + 1.0D;
            double d11 = (double)(MathHelper.sin((float)l * (float)Math.PI / (float)this.blocksPerVein) + 1.0F) * d9 + 1.0D;
            int i1 = MathHelper.floor_double(d6 - d10 / 2.0D);
            int j1 = MathHelper.floor_double(d7 - d11 / 2.0D);
            int k1 = MathHelper.floor_double(d8 - d10 / 2.0D);
            int l1 = MathHelper.floor_double(d6 + d10 / 2.0D);
            int i2 = MathHelper.floor_double(d7 + d11 / 2.0D);
            int j2 = MathHelper.floor_double(d8 + d10 / 2.0D);

            for (int cx = i1; cx <= l1; ++cx) {
                double d12 = ((double)cx + 0.5D - d6) / (d10 / 2.0D);

                if (d12 * d12 < 1.0D) {
                    for (int cy = j1; cy <= i2; ++cy) {
                        double d13 = ((double)cy + 0.5D - d7) / (d11 / 2.0D);

                        if (d12 * d12 + d13 * d13 < 1.0D) {
                            for (int cz = k1; cz <= j2; ++cz) {
                                if(cx > 0 && cx < WorldHelpers.CHUNK_SIZE
                                		&& cz > 0 && cz < WorldHelpers.CHUNK_SIZE) {
	                                ExtendedBlockStorage storage = chunk.getBlockStorageArray()[y >> 4];
	                                if(storage != null) {
	                    	            Block oldBlock = chunk.getBlock(x, y, z);
	                    	            int oldMeta = chunk.getBlockMetadata(x, y, z);
	                    	            if(oldBlock != null
	                    	            		&& oldBlock != block
	                    	            		&& oldBlock.isReplaceableOreGen(chunk.worldObj, cx, cy, cz, replaceTarget)
	                    	            		&& !oldBlock.hasTileEntity(oldMeta)) { // We do not replace TE's, even if they are replacable.
	                    	            	storage.func_150818_a(x, y & 15, z, block); // Replace the block.
	                    	            	if(meta != NO_META) {
	                    	            		storage.setExtBlockMetadata(x, y & 15, z, meta);
	                    	            	}
	                    	            }
	                                }
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

	@Override
	public void retroGenerateChunk(NBTTagCompound tag, Chunk chunk,
			Random rand) {
		for(int k = 0; k < veinsPerChunk; k++){
            int x = rand.nextInt(16);
            int y = startY + rand.nextInt(endY - startY);
            int z = rand.nextInt(16);
            
            this.generate(chunk, rand, x, y, z);
        }
	}

	@Override
	public boolean shouldRetroGen(NBTTagCompound tag, int dimensionId) {
		return dimensionId == getDimensionId() && !tag.getBoolean(getUniqueName());
	}

	@Override
	public void afterRetroGen(NBTTagCompound tag) {
		tag.setBoolean(getUniqueName(), true);
	}

	/**
	 * @return the dimensionId
	 */
	public int getDimensionId() {
		return dimensionId;
	}
}
