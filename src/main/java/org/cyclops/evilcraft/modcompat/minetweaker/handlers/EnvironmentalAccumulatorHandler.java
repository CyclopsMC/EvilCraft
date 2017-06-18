package org.cyclops.evilcraft.modcompat.minetweaker.handlers;

import mezz.jei.api.recipe.IRecipeWrapper;
import minetweaker.api.item.IItemStack;
import org.cyclops.cyclopscore.modcompat.jei.IJeiRecipeWrapperWrapper;
import org.cyclops.cyclopscore.modcompat.minetweaker.handlers.RecipeRegistryHandler;
import org.cyclops.cyclopscore.recipe.custom.Recipe;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.evilcraft.block.EnvironmentalAccumulator;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeComponent;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeProperties;
import org.cyclops.evilcraft.core.weather.WeatherType;
import org.cyclops.evilcraft.modcompat.jei.environmentalaccumulator.EnvironmentalAccumulatorRecipeJEI;
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

    @Override
    protected IJeiRecipeWrapperWrapper<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> createJeiWrapperWrapper() {
        return new IJeiRecipeWrapperWrapper<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties>() {
            @Override
            public IRecipeWrapper wrap(IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> recipe) {
                return new EnvironmentalAccumulatorRecipeJEI(recipe);
            }
        };
    }

    protected static WeatherType getWeather(String weather) {
        return Objects.requireNonNull(WeatherType.valueOf(weather), "Could not find a weather by name " + weather);
    }

    @ZenMethod
    public static void addRecipe(IItemStack inputStack, String inputWeather,
                                 IItemStack outputStack, String outputWeather,
                                 int duration, int cooldownTime, double processingSpeed) {
        INSTANCE.add(new Recipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties>(
                new EnvironmentalAccumulatorRecipeComponent(RecipeRegistryHandler.toStack(inputStack), getWeather(inputWeather)),
                new EnvironmentalAccumulatorRecipeComponent(RecipeRegistryHandler.toStack(outputStack), getWeather(outputWeather)),
                new EnvironmentalAccumulatorRecipeProperties(duration, cooldownTime, processingSpeed)));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack inputStack, String inputWeather,
                                    IItemStack outputStack, String outputWeather,
                                    int duration, int cooldownTime, double processingSpeed) {
        INSTANCE.add(new Recipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties>(
                new EnvironmentalAccumulatorRecipeComponent(RecipeRegistryHandler.toStack(inputStack), getWeather(inputWeather)),
                new EnvironmentalAccumulatorRecipeComponent(RecipeRegistryHandler.toStack(outputStack), getWeather(outputWeather)),
                new EnvironmentalAccumulatorRecipeProperties(duration, cooldownTime, processingSpeed)));
    }

    @ZenMethod
    public static void removeRecipesWithOutput(IItemStack outputStack, String outputWeather) {
        INSTANCE.remove(
                new EnvironmentalAccumulatorRecipeComponent(RecipeRegistryHandler.toStack(outputStack), getWeather(outputWeather))
        );
    }
}
