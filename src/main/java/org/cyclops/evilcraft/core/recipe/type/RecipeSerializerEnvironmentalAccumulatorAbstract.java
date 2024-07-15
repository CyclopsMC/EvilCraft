package org.cyclops.evilcraft.core.recipe.type;

import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import org.cyclops.cyclopscore.helper.RecipeSerializerHelpers;
import org.cyclops.cyclopscore.recipe.ItemStackFromIngredient;
import org.cyclops.evilcraft.blockentity.BlockEntityBloodInfuserConfig;
import org.cyclops.evilcraft.core.weather.WeatherType;

import java.util.Optional;

/**
 * Recipe serializer for abstract environmental accumulator recipes
 * @author rubensworks
 */
public abstract class RecipeSerializerEnvironmentalAccumulatorAbstract<T extends RecipeEnvironmentalAccumulator>
        implements RecipeSerializer<T> {

    protected final MapCodec<T> codec = RecordCodecBuilder.mapCodec(
            builder -> builder.group(
                            Ingredient.CODEC_NONEMPTY.fieldOf("input_item").forGetter(RecipeEnvironmentalAccumulator::getInputIngredient),
                            WeatherType.CODEC.fieldOf("input_weather").forGetter(RecipeEnvironmentalAccumulator::getInputWeather),
                            RecipeSerializerHelpers.getCodecItemStackOrTag(() -> BlockEntityBloodInfuserConfig.recipeTagOutputModPriorities).fieldOf("output_item").forGetter(RecipeEnvironmentalAccumulator::getOutputItem),
                            WeatherType.CODEC.fieldOf("output_weather").forGetter(RecipeEnvironmentalAccumulator::getOutputWeather),
                            Codec.INT.optionalFieldOf("duration").forGetter(RecipeEnvironmentalAccumulator::getDurationRaw),
                            Codec.INT.optionalFieldOf("cooldown_time").forGetter(RecipeEnvironmentalAccumulator::getCooldownTimeRaw),
                            Codec.FLOAT.optionalFieldOf("processing_speed").forGetter(RecipeEnvironmentalAccumulator::getProcessingSpeedRaw)
                    )
                    .apply(builder, this::createRecipe)
    );
    protected final StreamCodec<RegistryFriendlyByteBuf, T> STREAM_CODEC = NeoForgeStreamCodecs.composite(
            Ingredient.CONTENTS_STREAM_CODEC, RecipeEnvironmentalAccumulator::getInputIngredient,
            WeatherType.STREAM_CODEC, RecipeEnvironmentalAccumulator::getInputWeather,
            RecipeSerializerHelpers.STREAM_CODEC_ITEMSTACK_OR_TAG, RecipeEnvironmentalAccumulator::getOutputItem,
            WeatherType.STREAM_CODEC, RecipeEnvironmentalAccumulator::getOutputWeather,
            ByteBufCodecs.optional(ByteBufCodecs.INT), RecipeEnvironmentalAccumulator::getDurationRaw,
            ByteBufCodecs.optional(ByteBufCodecs.INT), RecipeEnvironmentalAccumulator::getCooldownTimeRaw,
            ByteBufCodecs.optional(ByteBufCodecs.FLOAT), RecipeEnvironmentalAccumulator::getProcessingSpeedRaw,
            this::createRecipe
    );

    @Override
    public MapCodec<T> codec() {
        return this.codec;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
        return STREAM_CODEC;
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
}
