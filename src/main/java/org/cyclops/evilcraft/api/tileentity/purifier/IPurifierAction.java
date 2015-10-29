package org.cyclops.evilcraft.api.tileentity.purifier;

import net.minecraft.item.ItemStack;
import org.cyclops.evilcraft.tileentity.TilePurifier;

/**
 * Actions that can be registered in the {@link IPurifierActionRegistry}.
 * @author rubensworks
 *
 */
public interface IPurifierAction {

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
     * @return If the Purifier can work in the current state.
     */
    public boolean canWork(TilePurifier tile);

    /**
     * Execute a valid Purifier action for the given Purifier tile.
     * @param tile The Purifier tile.
     * @return If work is done
     */
    public boolean work(TilePurifier tile);
    
}
