package evilcraft.api.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

/**
 * {@link ItemBlock} that can be used for blocks that have a tile entity with a fluid container.
 * @author rubensworks
 *
 */
public class ItemBlockFluidContainer extends ItemBlockNBT {
    
    /**
     * Make a new instance.
     * @param block The block instance.
     */
    public ItemBlockFluidContainer(Block block) {
        super(block);
    }

}
