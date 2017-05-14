package org.cyclops.evilcraft.modcompat.minetweaker.handlers;

import com.blamejared.mtlib.helpers.InputHelper;
import minetweaker.api.item.IItemStack;
import org.cyclops.cyclopscore.modcompat.minetweaker.handlers.RecipeRegistryHandler;
import org.cyclops.cyclopscore.recipe.custom.Recipe;
import org.cyclops.evilcraft.block.EnvironmentalAccumulator;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeComponent;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeProperties;
import org.cyclops.evilcraft.core.weather.WeatherType;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Objects;

@ZenClass("mods.evilcraft.EnvironmentalAccumulator")
public class EnvironmentalAccumulatorHandler extends RecipeRegistryHandler<EnvironmentalAccumulator, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> {

    private static final EnvironmentalAccumulatorHandler INSTANCE = new EnvironmentalAccumulatorHandler();

    @Override
    protected EnvironmentalAccumulator getMachine() {
        return EnvironmentalAccumulator.getInstance();
    }

    @Override
    protected String getRegistryName() {
        return "EnvironmentalAccumulator";
    }

    protected static WeatherType getWeather(String weather) {
        return Objects.requireNonNull(WeatherType.valueOf(weather), "Could not find a weather by name " + weather);
    }

    @ZenMethod
    public static void addRecipe(IItemStack inputStack, String inputWeather,
                                 IItemStack outputStack, String outputWeather,
                                 int duration, int cooldownTime, double processingSpeed) {
        INSTANCE.add(new Recipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties>(
                new EnvironmentalAccumulatorRecipeComponent(InputHelper.toStack(inputStack), getWeather(inputWeather)),
                new EnvironmentalAccumulatorRecipeComponent(InputHelper.toStack(outputStack), getWeather(outputWeather)),
                new EnvironmentalAccumulatorRecipeProperties(duration, cooldownTime, processingSpeed)));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack inputStack, String inputWeather,
                                    IItemStack outputStack, String outputWeather,
                                    int duration, int cooldownTime, double processingSpeed) {
        INSTANCE.add(new Recipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties>(
                new EnvironmentalAccumulatorRecipeComponent(InputHelper.toStack(inputStack), getWeather(inputWeather)),
                new EnvironmentalAccumulatorRecipeComponent(InputHelper.toStack(outputStack), getWeather(outputWeather)),
                new EnvironmentalAccumulatorRecipeProperties(duration, cooldownTime, processingSpeed)));
    }

    @ZenMethod
    public static void removeRecipesWithOutput(IItemStack outputStack, String outputWeather) {
        INSTANCE.remove(
                new EnvironmentalAccumulatorRecipeComponent(InputHelper.toStack(outputStack), getWeather(outputWeather))
        );
    }
}
