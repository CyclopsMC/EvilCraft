package evilcraft.api.recipes;

import net.minecraftforge.fluids.FluidStack;

/**
 * Interface for recipe components that hold a {@link net.minecraftforge.fluids.FluidStack}.
 * @author immortaleeb
 */
public interface IFluidStackRecipeComponent {
    /**
     * @return Returns the FluidStack held by this recipe component.
     */
    public FluidStack getFluidStack();
}
