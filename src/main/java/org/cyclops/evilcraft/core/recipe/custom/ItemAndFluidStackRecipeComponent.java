package org.cyclops.evilcraft.core.recipe.custom;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeInput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeOutput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeProperties;
import org.cyclops.cyclopscore.recipe.custom.component.*;

import java.util.List;

/**
 * A {@link org.cyclops.cyclopscore.recipe.custom.api.IRecipe} component that holds an {@link net.minecraft.item.ItemStack}
 * and a {@link net.minecraftforge.fluids.FluidStack}.
 * @author immortaleeb
 */
public class ItemAndFluidStackRecipeComponent implements IRecipeInput, IRecipeOutput, IRecipeProperties, IItemStackRecipeComponent, IFluidStackRecipeComponent {
    private final ItemStackRecipeComponent itemStack;
    private final FluidStackRecipeComponent fluidStack;

    public ItemAndFluidStackRecipeComponent(ItemStack itemStack, FluidStack fluidStack) {
        this.itemStack = new ItemStackRecipeComponent(itemStack);
        this.fluidStack = new FluidStackRecipeComponent(fluidStack);
    }

    public ItemAndFluidStackRecipeComponent(String key, FluidStack fluidStack) {
        this.itemStack = new OreDictItemStackRecipeComponent(key);
        this.fluidStack = new FluidStackRecipeComponent(fluidStack);
    }

    public ItemStack getItemStack() {
        return itemStack.getItemStack();
    }

    public List<ItemStack> getItemStacks() {
        return itemStack.getItemStacks();
    }

    public FluidStack getFluidStack() {
        return fluidStack.getFluidStack();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemAndFluidStackRecipeComponent)) return false;

        ItemAndFluidStackRecipeComponent that = (ItemAndFluidStackRecipeComponent) o;

        if (!fluidStack.equals(that.fluidStack)) return false;
        if (!itemStack.equals(that.itemStack)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = itemStack.hashCode();
        result = 31 * result + fluidStack.hashCode();
        return result;
    }
}
