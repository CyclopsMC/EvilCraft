package org.cyclops.evilcraft.core.recipe.custom;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeInput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeOutput;
import org.cyclops.cyclopscore.recipe.custom.component.IIngredientRecipeComponent;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientRecipeComponent;
import org.cyclops.evilcraft.block.EnvironmentalAccumulator;
import org.cyclops.evilcraft.core.weather.WeatherType;

import java.util.List;

/**
 * A recipe component that can be used in {@link org.cyclops.cyclopscore.recipe.custom.api.IRecipe}S for the
 * {@link EnvironmentalAccumulator}.
 * @author immortaleeb
 */
public class EnvironmentalAccumulatorRecipeComponent implements IRecipeInput, IRecipeOutput, IIngredientRecipeComponent, IWeatherTypeRecipeComponent {
    private final IngredientRecipeComponent ingredient;
    private final WeatherTypeRecipeComponent weatherType;

    public EnvironmentalAccumulatorRecipeComponent(Ingredient ingredient, WeatherType weatherType) {
        this.ingredient = new IngredientRecipeComponent(ingredient);
        this.weatherType = new WeatherTypeRecipeComponent(weatherType);
    }

    public EnvironmentalAccumulatorRecipeComponent(ItemStack itemStack, WeatherType weatherType) {
        this.ingredient = new IngredientRecipeComponent(itemStack);
        this.weatherType = new WeatherTypeRecipeComponent(weatherType);
    }

    @Override
    public Ingredient getIngredient() {
        return ingredient.getIngredient();
    }

    @Override
    public ItemStack getFirstItemStack() {
        return ingredient.getFirstItemStack();
    }

    /**
     * Get the itemstack and merge its nbt data from the given itemstack.
     * @param inputStack The stack to copy nbt data from.
     * @return The new itemstack.
     */
    public ItemStack getConditionalItemStack(ItemStack inputStack) {
        ItemStack itemStack = getFirstItemStack().copy();
        if(inputStack != null && inputStack.hasTagCompound()) {
            if(!itemStack.hasTagCompound()) {
                itemStack.setTagCompound(new NBTTagCompound());
            }
            for (String key : inputStack.getTagCompound().getKeySet()) {
                if(!itemStack.getTagCompound().hasKey(key)) {
                    itemStack.getTagCompound().setTag(key, inputStack.getTagCompound().getTag(key));
                }
            }
        }
        return itemStack;
    }

    public List<ItemStack> getItemStacks() {
        return ingredient.getItemStacks();
    }

    @Override
    public WeatherType getWeatherType() {
        return weatherType.getWeatherType();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnvironmentalAccumulatorRecipeComponent)) return false;

        EnvironmentalAccumulatorRecipeComponent that = (EnvironmentalAccumulatorRecipeComponent) o;

        if (!ingredient.equals(that.ingredient)) return false;
        if (!weatherType.equals(that.weatherType)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ingredient.hashCode();
        result = 31 * result + weatherType.hashCode();
        return result;
    }
}
