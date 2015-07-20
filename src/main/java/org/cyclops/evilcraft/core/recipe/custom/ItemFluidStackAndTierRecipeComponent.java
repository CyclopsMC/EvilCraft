package org.cyclops.evilcraft.core.recipe.custom;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * Input component with tier.
 * @author rubensworks
 */
public class ItemFluidStackAndTierRecipeComponent extends ItemAndFluidStackRecipeComponent {

    private int tier;

    public ItemFluidStackAndTierRecipeComponent(ItemStack itemStack, FluidStack fluidStack, int tier) {
        super(itemStack, fluidStack);
        this.tier = tier;
    }

    public ItemFluidStackAndTierRecipeComponent(String key, FluidStack fluidStack, int tier) {
        super(key, fluidStack);
        this.tier = tier;
    }

    public int getTier() {
        return this.tier;
    }

    @Override
    public boolean equals(Object o) {
        ItemFluidStackAndTierRecipeComponent oc = ((ItemFluidStackAndTierRecipeComponent) o);
        return super.equals(o) && (oc.getTier() >= this.tier || this.tier == -1 || oc.getTier() == -1);
    }

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + tier;
    }
}
