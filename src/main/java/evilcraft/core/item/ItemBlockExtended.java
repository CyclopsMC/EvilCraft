package evilcraft.core.item;

import evilcraft.core.IInformationProvider;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

/**
 * An extended {@link ItemBlock} that will automatically add information to the blockState
 * item if that blockState implements {@link IInformationProvider}.
 * @author rubensworks
 *
 */
public class ItemBlockExtended extends ItemBlockMetadata {

    /**
     * Make a new instance.
     * @param block The blockState instance.
     */
    public ItemBlockExtended(Block block) {
    	super(block);

    }
    
}
