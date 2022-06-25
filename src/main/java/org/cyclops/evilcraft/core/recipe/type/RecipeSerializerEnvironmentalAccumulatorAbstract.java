package org.cyclops.evilcraft.core.recipe.type;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Either;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.cyclops.cyclopscore.helper.RecipeSerializerHelpers;
import org.cyclops.cyclopscore.recipe.ItemStackFromIngredient;
import org.cyclops.evilcraft.core.weather.WeatherType;

import javax.annotation.Nullable;
import java.util.Locale;

/**
 * Recipe serializer for abstract environmental accumulator recipes
 * @author rubensworks
 */
public abstract class RecipeSerializerEnvironmentalAccumulatorAbstract<T extends RecipeEnvironmentalAccumulator>
        implements RecipeSerializer<T> {

    protected WeatherType getWeatherType(String type) throws JsonSyntaxException {
        WeatherType weather = WeatherType.valueOf(type);
        if(weather == null) {
            throw new JsonSyntaxException(String.format("Could not found the weather '%s'", type));
        }
        return weather;
    }

    protected abstract T createRecipe(ResourceLocation id,
                                      Ingredient inputIngredient, WeatherType inputWeather,
                                      Either<ItemStack, ItemStackFromIngredient> outputItem, WeatherType outputWeather,
                                      int duration, int cooldownTime, float processingSpeed);

    @Override
    public T fromJson(ResourceLocation recipeId, JsonObject json) {
        JsonObject result = GsonHelper.getAsJsonObject(json, "result");

        // Input
        Ingredient inputIngredient = RecipeSerializerHelpers.getJsonIngredient(json, "item", false);
        WeatherType inputWeather = getWeatherType(GsonHelper.getAsString(json, "weather"));

        // Output
        Either<ItemStack, ItemStackFromIngredient> outputItemStack = RecipeSerializerHelpers.getJsonItemStackOrTag(result, false);
        WeatherType outputWeather = getWeatherType(GsonHelper.getAsString(result, "weather"));

        // Other stuff
        int duration = GsonHelper.getAsInt(json, "duration", -1);
        int cooldownTime = GsonHelper.getAsInt(json, "cooldownTime", -1);
        float processingSpeed = GsonHelper.getAsFloat(json, "processingSpeed", -1.0F);

        return createRecipe(recipeId, inputIngredient, inputWeather, outputItemStack, outputWeather, duration, cooldownTime, processingSpeed);
    }

    @Nullable
    @Override
    public T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        // Input
        Ingredient inputIngredient = Ingredient.fromNetwork(buffer);
        WeatherType inputWeather = getWeatherType(buffer.readUtf(20));

        // Output
        Either<ItemStack, ItemStackFromIngredient> outputItem = RecipeSerializerHelpers.readItemStackOrItemStackIngredient(buffer);
        WeatherType outputWeather = getWeatherType(buffer.readUtf(20));

        // Other stuff
        int duration = buffer.readVarInt();
        int cooldownTime = buffer.readVarInt();
        float processingSpeed = buffer.readFloat();

        return createRecipe(recipeId, inputIngredient, inputWeather, outputItem, outputWeather, duration, cooldownTime, processingSpeed);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, RecipeEnvironmentalAccumulator recipe) {
        // Input
        recipe.getInputIngredient().toNetwork(buffer);
        buffer.writeUtf(recipe.getInputWeather().toString().toUpperCase(Locale.ENGLISH));

        // Output
        RecipeSerializerHelpers.writeItemStackOrItemStackIngredient(buffer, recipe.getOutputItem());
        buffer.writeUtf(recipe.getOutputWeather().toString().toUpperCase(Locale.ENGLISH));

        // Other stuff
        buffer.writeVarInt(recipe.getDuration());
        buffer.writeVarInt(recipe.getCooldownTime());
        buffer.writeFloat(recipe.getProcessingSpeed());
    }
}
