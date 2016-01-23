package org.cyclops.evilcraft.modcompat.jei.environmentalaccumulator;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.evilcraft.block.EnvironmentalAccumulator;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeComponent;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeProperties;
import org.cyclops.evilcraft.core.weather.WeatherType;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Recipe wrapper for Envir Acc recipes
 * @author rubensworks
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class EnvironmentalAccumulatorRecipeJEI extends BlankRecipeWrapper {

    private final WeatherType inputWeather;
    private final WeatherType outputWeather;
    private final List<ItemStack> input;
    private final List<ItemStack> output;
    private final EnvironmentalAccumulatorRecipeProperties properties;

    public EnvironmentalAccumulatorRecipeJEI(IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> recipe) {
        recipe.getInput().getWeatherType();
        this.input = recipe.getInput().getItemStacks();
        this.inputWeather = recipe.getInput().getWeatherType();
        this.output = recipe.getOutput().getItemStacks();
        this.outputWeather = recipe.getOutput().getWeatherType();
        this.properties = recipe.getProperties();
    }

    @Override
    public List getInputs() {
        return input;
    }

    @Override
    public List getOutputs() {
        return output;
    }

    public static List<EnvironmentalAccumulatorRecipeJEI> getAllRecipes() {
        return Lists.transform(EnvironmentalAccumulator.getInstance().getRecipeRegistry().allRecipes(), new Function<IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties>, EnvironmentalAccumulatorRecipeJEI>() {
            @Nullable
            @Override
            public EnvironmentalAccumulatorRecipeJEI apply(@Nullable IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> input) {
                return new EnvironmentalAccumulatorRecipeJEI(input);
            }
        });
    }

    public EnvironmentalAccumulatorRecipeProperties getProperties() {
        return this.properties;
    }
}
