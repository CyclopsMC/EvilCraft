package org.cyclops.evilcraft.core.recipe.type;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import org.cyclops.evilcraft.core.recipe.type.RecipeEnvironmentalAccumulatorBiomeExtract;
import org.cyclops.evilcraft.core.recipe.type.RecipeSerializerEnvironmentalAccumulatorAbstract;
import org.cyclops.evilcraft.core.weather.WeatherType;

/**
 * Recipe serializer for environmental accumulator recipes
 * @author rubensworks
 */
public class RecipeSerializerEnvironmentalAccumulatorBiomeExtract extends RecipeSerializerEnvironmentalAccumulatorAbstract<RecipeEnvironmentalAccumulatorBiomeExtract> {
    @Override
    protected RecipeEnvironmentalAccumulatorBiomeExtract createRecipe(ResourceLocation id, Ingredient inputIngredient, WeatherType inputWeather, ItemStack outputItem, WeatherType outputWeather, int duration, int cooldownTime, float processingSpeed) {
        return new RecipeEnvironmentalAccumulatorBiomeExtract(id, inputIngredient, inputWeather, outputItem, outputWeather, duration, cooldownTime, processingSpeed);
    }
}
