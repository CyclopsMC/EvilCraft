package evilcraft.core.block;

import net.minecraft.item.ItemStack;

/**
 * Interface for blocks that have a tank.
 * @author rubensworks
 *
 */
public interface IBlockTank {

	/**
     * Get the NBT name for the inner tank.
     * @return The NBT key for the tank.
     */
    public String getTankNBTName();
    /**
     * Get the maximal tank capacity.
     * @param itemStack The item stack.
     * @return The maximal tank capacity in mB.
     */
    public int getTankCapacity(ItemStack itemStack);
	
}
