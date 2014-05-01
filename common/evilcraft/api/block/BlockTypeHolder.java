package evilcraft.api.block;

import net.minecraft.block.Block;

/**
 * A holder class for a {@link Block} and a metadata.
 * @author rubensworks
 *
 */
public class BlockTypeHolder {

    private Block block;
    private int meta;
    
    /**
     * Make a new instance.
     * @param block The block.
     * @param meta The meta for that block.
     */
    public BlockTypeHolder(Block block, int meta) {
        this.block = block;
        this.meta = meta;
    }
    
    /**
     * Make a new instance.
     * @param block The block.
     */
    public BlockTypeHolder(Block block) {
        this(block, 0);
    }

    /**
     * @return the block
     */
    public Block getBlock() {
        return block;
    }

    /**
     * @param block the block to set
     */
    public void setBlock(Block block) {
        this.block = block;
    }

    /**
     * @return the meta
     */
    public int getMeta() {
        return meta;
    }

    /**
     * @param meta the meta to set
     */
    public void setMeta(int meta) {
        this.meta = meta;
    }
    
    
    
}
