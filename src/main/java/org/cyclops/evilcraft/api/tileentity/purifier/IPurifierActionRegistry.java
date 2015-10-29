package org.cyclops.evilcraft.api.tileentity.purifier;

import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.init.IRegistry;
import org.cyclops.evilcraft.tileentity.TilePurifier;

/**
 * Registry for {@link IPurifierAction} instances.
 * @author rubensworks
 */
public interface IPurifierActionRegistry extends IRegistry {
	
	/**
     * Register a new repair action.
     * @param purifyAction The repair action instance.
     */
    public void register(IPurifierAction purifyAction);
    
    /**
     * Check if the given item can be inserted into the Purifier to purify.
     * @param itemStack The {@link ItemStack} that could be inserted.
     * @return If the given item can be inserted into the Purifier.
     */
    public boolean isItemValidForMainSlot(ItemStack itemStack);

    /**
     * Check if the given item can be inserted into the additional slot of the Purifier.
     * @param itemStack The {@link ItemStack} that could be inserted.
     * @return If the given item can be inserted into the additional slot of the Purifier.
     */
    public boolean isItemValidForAdditionalSlot(ItemStack itemStack);
    
    /**
     * Check if he Purifier can work in the current state.
     * @param tile The Purifier tile.
     * @return The action id for the workable action.
     */
    public int canWork(TilePurifier tile);
    
    /**
     * Execute a valid Purifier action for the given Purifier tile.
     * @param actionId -1 if none of the actions apply, otherwise the actionId that applies.
     * @param tile The Purifier tile.
     * @return If work is done
     */
    public boolean work(int actionId, TilePurifier tile);
    
}
