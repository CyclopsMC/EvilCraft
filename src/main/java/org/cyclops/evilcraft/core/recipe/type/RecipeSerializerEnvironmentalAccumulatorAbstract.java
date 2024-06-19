package org.cyclops.evilcraft.core.recipe.type;

import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.cyclops.cyclopscore.helper.RecipeSerializerHelpers;
import org.cyclops.cyclopscore.recipe.ItemStackFromIngredient;
import org.cyclops.evilcraft.blockentity.BlockEntityBloodInfuserConfig;
import org.cyclops.evilcraft.core.weather.WeatherType;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Optional;

/**
 * Recipe serializer for abstract environmental accumulator recipes
 * @author rubensworks
 */
public abstract class RecipeSerializerEnvironmentalAccumulatorAbstract<T extends RecipeEnvironmentalAccumulator>
        implements RecipeSerializer<T> {

    protected final Codec<T> codec = RecordCodecBuilder.create(
            builder -> builder.group(
                            Ingredient.CODEC_NONEMPTY.fieldOf("input_item").forGetter(RecipeEnvironmentalAccumulator::getInputIngredient),
                            WeatherType.CODEC.fieldOf("input_weather").forGetter(RecipeEnvironmentalAccumulator::getInputWeather),
                            RecipeSerializerHelpers.getCodecItemStackOrTag(() -> BlockEntityBloodInfuserConfig.recipeTagOutputModPriorities).fieldOf("output_item").forGetter(RecipeEnvironmentalAccumulator::getOutputItem),
                            WeatherType.CODEC.fieldOf("output_weather").forGetter(RecipeEnvironmentalAccumulator::getOutputWeather),
                            ExtraCodecs.strictOptionalField(Codec.INT, "duration").forGetter(RecipeEnvironmentalAccumulator::getDurationRaw),
                            ExtraCodecs.strictOptionalField(Codec.INT, "cooldown_time").forGetter(RecipeEnvironmentalAccumulator::getCooldownTimeRaw),
                            ExtraCodecs.strictOptionalField(Codec.FLOAT, "processing_speed").forGetter(RecipeEnvironmentalAccumulator::getProcessingSpeedRaw)
                    )
                    .apply(builder, this::createRecipe)
    );

    @Override
    public Codec<T> codec() {
        return this.codec;
    }

    protected WeatherType getWeatherType(String type) throws JsonSyntaxException {
        WeatherType weather = WeatherType.valueOf(type);
        if(weather == null) {
            throw new JsonSyntaxException(String.format("Could not found the weather '%s'", type));
        }
        return weather;
    }

    protected abstract T createRecipe(Ingredient inputIngredient, WeatherType inputWeather,
                                      Either<ItemStack, ItemStackFromIngredient> outputItem, WeatherType outputWeather,
                                      Optional<Integer> duration, Optional<Integer> cooldownTime, Optional<Float> processingSpeed);

    @Nullable
    @Override
    public T fromNetwork(FriendlyByteBuf buffer) {
        // Input
        Ingredient inputIngredient = Ingredient.fromNetwork(buffer);
        WeatherType inputWeather = getWeatherType(buffer.readUtf(20));

        // Output
        Either<ItemStack, ItemStackFromIngredient> outputItem = RecipeSerializerHelpers.readItemStackOrItemStackIngredient(buffer);
        WeatherType outputWeather = getWeatherType(buffer.readUtf(20));

        // Other stuff
        Optional<Integer> duration = RecipeSerializerHelpers.readOptionalFromNetwork(buffer, FriendlyByteBuf::readVarInt);
        Optional<Integer> cooldownTime = RecipeSerializerHelpers.readOptionalFromNetwork(buffer, FriendlyByteBuf::readVarInt);
        Optional<Float> processingSpeed = RecipeSerializerHelpers.readOptionalFromNetwork(buffer, FriendlyByteBuf::readFloat);

        return createRecipe(inputIngredient, inputWeather, outputItem, outputWeather, duration, cooldownTime, processingSpeed);
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
        RecipeSerializerHelpers.writeOptionalToNetwork(buffer, recipe.getDurationRaw(), FriendlyByteBuf::writeVarInt);
        RecipeSerializerHelpers.writeOptionalToNetwork(buffer, recipe.getCooldownTimeRaw(), FriendlyByteBuf::writeVarInt);
        RecipeSerializerHelpers.writeOptionalToNetwork(buffer, recipe.getProcessingSpeedRaw(), FriendlyByteBuf::writeFloat);
    }
}
