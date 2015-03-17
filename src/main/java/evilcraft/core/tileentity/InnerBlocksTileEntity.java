package evilcraft.core.tileentity;

import evilcraft.core.helper.MinecraftHelpers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

/**
 * A TileEntity with an inner blockState.
 * @author rubensworks
 *
 */
public abstract class InnerBlocksTileEntity extends TickingEvilCraftTileEntity {
    
	@NBTPersist
	private String blockName = null;
    @NBTPersist
    private int meta = 0;
    
	/**
	 * Set the inner blockState.
	 * @param blockState The blockState state.
	 */
    public void setInnerBlockState(IBlockState blockState) {
        this.meta = blockState.getBlock().getMetaFromState(blockState);
    	this.blockName = (String) Block.blockRegistry.getNameForObject(blockState.getBlock());
    }
    
    /**
     * Get the inner blockState.
     * @return The inner blockState.
     */
	public IBlockState getInnerBlockState() {
		return Block.getBlockFromName(blockName).getStateFromMeta(this.meta);
    }
	
	protected void onDeprecationTrigger() {
		
	}
    
}
