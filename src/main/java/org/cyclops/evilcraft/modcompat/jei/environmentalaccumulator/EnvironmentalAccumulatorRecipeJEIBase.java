package org.cyclops.evilcraft.modcompat.jei.environmentalaccumulator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeComponent;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeProperties;
import org.cyclops.evilcraft.core.weather.WeatherType;

import java.util.List;

/**
 * Base recipe wrapper for Envir Acc recipes
 * @author rubensworks
 */
@EqualsAndHashCode(callSuper = false)
@Data
public abstract class EnvironmentalAccumulatorRecipeJEIBase extends BlankRecipeWrapper {

    private final IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> recipe;
    private final WeatherType inputWeather;
    private final WeatherType outputWeather;
    private final List<ItemStack> input;
    private final List<ItemStack> output;
    private final EnvironmentalAccumulatorRecipeProperties properties;

    public EnvironmentalAccumulatorRecipeJEIBase(IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> recipe) {
        this.recipe = recipe;
        this.input = recipe.getInput().getItemStacks();
        this.inputWeather = recipe.getInput().getWeatherType();
        this.output = recipe.getOutput().getItemStacks();
        this.outputWeather = recipe.getOutput().getWeatherType();
        this.properties = recipe.getProperties();
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(ItemStack.class, input);
        ingredients.setOutputs(ItemStack.class, output);
    }

    public EnvironmentalAccumulatorRecipeProperties getProperties() {
        return this.properties;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof EnvironmentalAccumulatorRecipeJEIBase && ((EnvironmentalAccumulatorRecipeJEIBase) o).recipe.equals(this.recipe);
    }

    @Override
    public int hashCode() {
        return 1 | this.recipe.hashCode();
    }
}
