package org.cyclops.evilcraft.item;

import net.minecraft.item.ItemStack;

/**
 * Interface for items that are empowerable.
 * @author rubensworks
 *
 */
public interface IItemEmpowerable {

	/**
     * If the given ItemStack is an empowered item.
     * @param itemStack The ItemStack to check.
     * @return If it is an empowered item.
     */
    public boolean isEmpowered(ItemStack itemStack);
    
    /**
     * Set the given ItemStack as an empowered item.
     * @param itemStack The ItemStack to check.
     * @return The empowered item.
     */
    public ItemStack empower(ItemStack itemStack);
    
}
