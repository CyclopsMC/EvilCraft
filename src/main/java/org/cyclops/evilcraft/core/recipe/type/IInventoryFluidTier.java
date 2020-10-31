package org.cyclops.evilcraft.core.recipe.type;

import net.minecraft.inventory.IInventory;
import org.cyclops.cyclopscore.recipe.type.IInventoryFluid;

/**
 * @author rubensworks
 */
public interface IInventoryFluidTier extends IInventoryFluid {
    public int getTier();
}
