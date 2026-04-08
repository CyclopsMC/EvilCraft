package org.cyclops.evilcraft.core.recipe.type;

import com.mojang.datafixers.util.Either;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import org.cyclops.cyclopscore.recipe.ItemStackFromIngredient;
import org.cyclops.evilcraft.core.weather.WeatherType;

import java.util.Optional;

/**
 * Recipe serializer for environmental accumulator recipes
 * @author rubensworks
 */
public class RecipeSerializerEnvironmentalAccumulator extends RecipeSerializerEnvironmentalAccumulatorAbstract<RecipeEnvironmentalAccumulator> {
    @Override
    protected RecipeEnvironmentalAccumulator createRecipe(Ingredient inputIngredient, WeatherType inputWeather,
                                                          Either<ItemStackTemplate, ItemStackFromIngredient> outputItem, WeatherType outputWeather,
                                                          Optional<Integer> duration, Optional<Integer> cooldownTime, Optional<Float> processingSpeed) {
        return new RecipeEnvironmentalAccumulator(inputIngredient, inputWeather, outputItem, outputWeather, duration, cooldownTime, processingSpeed);
    }
}
