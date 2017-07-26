package org.cyclops.evilcraft.modcompat.jei.environmentalaccumulator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.modcompat.jei.RecipeRegistryJeiRecipeWrapper;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeRegistry;
import org.cyclops.evilcraft.block.EnvironmentalAccumulator;
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
public abstract class EnvironmentalAccumulatorRecipeJEIBase<T extends EnvironmentalAccumulatorRecipeJEIBase<T>> extends RecipeRegistryJeiRecipeWrapper<EnvironmentalAccumulator, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties, T> {

    private final WeatherType inputWeather;
    private final WeatherType outputWeather;
    private final List<ItemStack> input;
    private final List<ItemStack> output;
    private final EnvironmentalAccumulatorRecipeProperties properties;

    public EnvironmentalAccumulatorRecipeJEIBase(IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> recipe) {
        super(recipe);
        this.input = recipe.getInput().getItemStacks();
        this.inputWeather = recipe.getInput().getWeatherType();
        this.output = recipe.getOutput().getItemStacks();
        this.outputWeather = recipe.getOutput().getWeatherType();
        this.properties = recipe.getProperties();
    }

    protected EnvironmentalAccumulatorRecipeJEIBase() {
        super(null);
        this.inputWeather = null;
        this.outputWeather = null;
        this.input = null;
        this.output = null;
        this.properties = null;
    }

    @Override
    protected IRecipeRegistry<EnvironmentalAccumulator, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> getRecipeRegistry() {
        return EnvironmentalAccumulator.getInstance().getRecipeRegistry();
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(ItemStack.class, getInputs());
        ingredients.setOutputs(ItemStack.class, getOutputs());
    }

    @Override
    public List getInputs() {
        return input;
    }

    @Override
    public List getOutputs() {
        return output;
    }

    public EnvironmentalAccumulatorRecipeProperties getProperties() {
        return this.properties;
    }

}
