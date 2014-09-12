package evilcraft.core.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import evilcraft.core.IInformationProvider;

/**
 * An extended {@link ItemBlock} that will automatically add information to the block
 * item if that block implements {@link IInformationProvider}.
 * @author rubensworks
 *
 */
public class ItemBlockExtended extends ItemBlockMetadata {

    /**
     * Make a new instance.
     * @param block The block instance.
     */
    public ItemBlockExtended(Block block) {
    	super(block);
    }
    
}
