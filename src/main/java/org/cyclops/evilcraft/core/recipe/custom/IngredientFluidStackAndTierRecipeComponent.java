package org.cyclops.evilcraft.core.recipe.custom;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreIngredient;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientAndFluidStackRecipeComponent;

/**
 * Input component with tier.
 * @author rubensworks
 */
public class IngredientFluidStackAndTierRecipeComponent extends IngredientAndFluidStackRecipeComponent {

    private int tier;

    public IngredientFluidStackAndTierRecipeComponent(Ingredient ingredient, FluidStack fluidStack, int tier) {
        super(ingredient, fluidStack);
        this.tier = tier;
    }

    public IngredientFluidStackAndTierRecipeComponent(String key, FluidStack fluidStack, int tier) {
        super(new OreIngredient(key), fluidStack);
        this.tier = tier;
    }

    public IngredientFluidStackAndTierRecipeComponent(ItemStack itemStack, FluidStack fluidStack, int tier) {
        super(itemStack, fluidStack);
        this.tier = tier;
    }

    public int getTier() {
        return this.tier;
    }

    @Override
    public boolean equals(Object o) {
        IngredientFluidStackAndTierRecipeComponent oc = ((IngredientFluidStackAndTierRecipeComponent) o);
        return super.equals(o) && (oc.getTier() >= this.tier || this.tier == -1 || oc.getTier() == -1);
    }

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + tier;
    }
}
