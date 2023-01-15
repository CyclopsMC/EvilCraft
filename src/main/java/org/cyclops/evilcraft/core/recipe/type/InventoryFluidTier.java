package org.cyclops.evilcraft.core.recipe.type;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.recipe.type.InventoryFluid;

/**
 * @author rubensworks
 */
public class InventoryFluidTier extends InventoryFluid implements IInventoryFluidTier {

    private final int tier;

    public InventoryFluidTier(NonNullList<ItemStack> itemStacks, NonNullList<FluidStack> fluidStacks, int tier) {
        super(itemStacks, fluidStacks);
        this.tier = tier;
    }

    @Override
    public int getTier() {
        return this.tier;
    }
}
