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
     * @param blockID The block ID.
     * @param block The block instance.
     */
    public ItemBlockFluidContainer(int blockID, Block block) {
        super(blockID, block);
    }

}
