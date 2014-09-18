package evilcraft.core.tileentity;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import evilcraft.core.helper.MinecraftHelpers;

/**
 * A TileEntity with an inner block.
 * @author rubensworks
 *
 */
public abstract class InnerBlocksTileEntity extends EvilCraftTileEntity {
	
	private static Block[] OLD_BLOCKS = {
        Blocks.grass,
        Blocks.dirt,
        Blocks.stone,
        Blocks.stonebrick,
        Blocks.cobblestone,
        Blocks.sand
        };
    
	@NBTPersist
	private String blockName = null;
    
	/**
	 * Set the inner block.
	 * @param block The block.
	 */
    public void setInnerBlock(Block block) {
    	this.blockName = block.blockRegistry.getNameForObject(block);
    	sendImmediateUpdate();
    }
    
    /**
     * Get the inner block.
     * @return The inner block.
     */
	public Block getInnerBlock() {
		if(blockName != null && !blockName.isEmpty()) {
	    	return Block.getBlockFromName(blockName);
		} else {
			// Backwards compatibility!
	    	int meta = getWorldObj().getBlockMetadata(xCoord, yCoord, zCoord);
	    	Block ret = OLD_BLOCKS[Math.min(OLD_BLOCKS.length - 1, meta)];
	    	setInnerBlock(ret);
	    	getWorldObj().setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
	    	onDeprecationTrigger();
	    	return ret;
		}
    }
	
	protected void onDeprecationTrigger() {
		
	}
    
}
