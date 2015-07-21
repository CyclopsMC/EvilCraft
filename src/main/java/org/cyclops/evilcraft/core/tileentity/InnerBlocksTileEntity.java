package org.cyclops.evilcraft.core.tileentity;

import lombok.experimental.Delegate;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import org.cyclops.cyclopscore.persist.nbt.NBTPersist;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;

/**
 * A TileEntity with an inner blockState.
 * @author rubensworks
 *
 */
public abstract class InnerBlocksTileEntity extends CyclopsTileEntity implements CyclopsTileEntity.ITickingTile {

    @Delegate
    private final ITickingTile tickingTileComponent = new TickingTileComponent(this);

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
    	this.blockName = Block.blockRegistry.getNameForObject(blockState.getBlock()).toString();
    }
    
    /**
     * Get the inner blockState.
     * @return The inner blockState.
     */
	public IBlockState getInnerBlockState() {
        if(blockName == null || blockName.isEmpty()) return Blocks.stone.getDefaultState();
		return Block.getBlockFromName(blockName).getStateFromMeta(this.meta);
    }
	
	protected void onDeprecationTrigger() {
		
	}
    
}
