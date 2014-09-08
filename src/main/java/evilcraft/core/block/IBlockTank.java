package evilcraft.core.block;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

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
    /**
     * Set the maximal tank capacity.
     * @param itemStack The item stack.
     * @param capacity The maximal tank capacity in mB.
     */
    public void setTankCapacity(ItemStack itemStack, int capacity);
    /**
     * Set the maximal tank capacity.
     * @param tag The tag of an item stack.
     * @param capacity The maximal tank capacity in mB.
     */
    public void setTankCapacity(NBTTagCompound tag, int capacity);
    /**
     * Get the maximum capacity possible for this item/block type.
     * Only makes sense to have a bigger value than {@link IBlockTank#getTankCapacity(ItemStack)}
     * if this capacity is variable.
     * @return The max capacity.
     */
    public int getMaxCapacity();
	
}
