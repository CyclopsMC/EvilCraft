package evilcraft.core.recipe.custom;

import evilcraft.api.recipes.custom.IRecipeInput;
import evilcraft.api.recipes.custom.IRecipeOutput;
import evilcraft.api.recipes.custom.IRecipeProperties;
import lombok.Data;
import net.minecraftforge.fluids.FluidStack;

/**
 * A {@link evilcraft.api.recipes.custom.IRecipe} component (input, output or properties) that holds an
 * {@link net.minecraftforge.fluids.FluidStack}.
 *
 * @author immortaleeb
 */
@Data
public class FluidStackRecipeComponent implements IRecipeInput, IRecipeOutput, IRecipeProperties, IFluidStackRecipeComponent {
    private final FluidStack fluidStack;

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof FluidStackRecipeComponent)) return false;
        FluidStackRecipeComponent that = (FluidStackRecipeComponent)object;

        if (this.fluidStack != null) {
            return this.fluidStack.getFluid() != null && this.fluidStack.getFluid().equals(that.fluidStack.getFluid());
        }

        return that.fluidStack == null;
    }

    @Override
    public int hashCode() {
        return fluidStack.getFluid().hashCode() + 90;
    }
}
