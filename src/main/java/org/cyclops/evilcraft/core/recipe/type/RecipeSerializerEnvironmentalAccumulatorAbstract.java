package org.cyclops.evilcraft.core.recipe.type;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.cyclops.cyclopscore.helper.RecipeSerializerHelpers;
import org.cyclops.evilcraft.core.weather.WeatherType;

import javax.annotation.Nullable;

/**
 * Recipe serializer for abstract environmental accumulator recipes
 * @author rubensworks
 */
public abstract class RecipeSerializerEnvironmentalAccumulatorAbstract<T extends RecipeEnvironmentalAccumulator> extends ForgeRegistryEntry<IRecipeSerializer<?>>
        implements IRecipeSerializer<T> {

    protected WeatherType getWeatherType(String type) throws JsonSyntaxException {
        WeatherType weather = WeatherType.valueOf(type);
        if(weather == null) {
            throw new JsonSyntaxException(String.format("Could not found the weather '%s'", type));
        }
        return weather;
    }

    protected abstract T createRecipe(ResourceLocation id,
                             Ingredient inputIngredient, WeatherType inputWeather,
                             ItemStack outputItem, WeatherType outputWeather,
                             int duration, int cooldownTime, float processingSpeed);

    @Override
    public T read(ResourceLocation recipeId, JsonObject json) {
        JsonObject result = JSONUtils.getJsonObject(json, "result");

        // Input
        Ingredient inputIngredient = RecipeSerializerHelpers.getJsonIngredient(json, "item", false);
        WeatherType inputWeather = getWeatherType(JSONUtils.getString(json, "weather"));

        // Output
        ItemStack outputItemStack = RecipeSerializerHelpers.getJsonItemStackOrTag(result, false);
        WeatherType outputWeather = getWeatherType(JSONUtils.getString(result, "weather"));

        // Other stuff
        int duration = JSONUtils.getInt(json, "duration", -1);
        int cooldownTime = JSONUtils.getInt(json, "cooldownTime", -1);
        float processingSpeed = JSONUtils.getFloat(json, "processingSpeed", -1.0F);

        return createRecipe(recipeId, inputIngredient, inputWeather, outputItemStack, outputWeather, duration, cooldownTime, processingSpeed);
    }

    @Nullable
    @Override
    public T read(ResourceLocation recipeId, PacketBuffer buffer) {
        // Input
        Ingredient inputIngredient = Ingredient.read(buffer);
        WeatherType inputWeather = getWeatherType(buffer.readString(20));

        // Output
        ItemStack outputItemStack = buffer.readItemStack();
        WeatherType outputWeather = getWeatherType(buffer.readString(20));

        // Other stuff
        int duration = buffer.readVarInt();
        int cooldownTime = buffer.readVarInt();
        float processingSpeed = buffer.readFloat();

        return createRecipe(recipeId, inputIngredient, inputWeather, outputItemStack, outputWeather, duration, cooldownTime, processingSpeed);
    }

    @Override
    public void write(PacketBuffer buffer, RecipeEnvironmentalAccumulator recipe) {
        // Input
        recipe.getInputIngredient().write(buffer);
        buffer.writeString(recipe.getInputWeather().toString());

        // Output
        buffer.writeItemStack(recipe.getOutputItem());
        buffer.writeString(recipe.getOutputWeather().toString());

        // Other stuff
        buffer.writeVarInt(recipe.getDuration());
        buffer.writeVarInt(recipe.getCooldownTime());
        buffer.writeFloat(recipe.getProcessingSpeed());
    }
}
