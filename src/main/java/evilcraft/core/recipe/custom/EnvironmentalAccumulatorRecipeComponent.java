package evilcraft.core.recipe.custom;

import evilcraft.api.recipes.custom.IRecipeInput;
import evilcraft.api.recipes.custom.IRecipeOutput;
import evilcraft.core.weather.WeatherType;
import net.minecraft.item.ItemStack;

/**
 * A recipe component that can be used in {@link evilcraft.api.recipes.custom.IRecipe}S for the
 * {@link evilcraft.block.EnvironmentalAccumulator}.
 * @author immortaleeb
 */
public class EnvironmentalAccumulatorRecipeComponent implements IRecipeInput, IRecipeOutput, IItemStackRecipeComponent, IWeatherTypeRecipeComponent {
    private final ItemStackRecipeComponent itemStack;
    private final WeatherTypeRecipeComponent weatherType;

    public EnvironmentalAccumulatorRecipeComponent(ItemStack itemStack, WeatherType weatherType) {
        this.itemStack = new ItemStackRecipeComponent(itemStack);
        this.weatherType = new WeatherTypeRecipeComponent(weatherType);
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack.getItemStack();
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

        if (!itemStack.equals(that.itemStack)) return false;
        if (!weatherType.equals(that.weatherType)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = itemStack.hashCode();
        result = 31 * result + weatherType.hashCode();
        return result;
    }
}
