package org.cyclops.evilcraft.core.recipe.display;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.cyclops.evilcraft.core.weather.WeatherType;

/**
 * @author rubensworks
 */
public record RecipeDisplayEnvironmentalAccumulator(
        SlotDisplay inputIngredient,
        WeatherType inputWeather,
        SlotDisplay outputItem,
        WeatherType outputWeather,
        SlotDisplay craftingStation,
        int duration,
        int cooldownTime,
        float processingSpeed
) implements RecipeDisplay {

    public static final MapCodec<RecipeDisplayEnvironmentalAccumulator> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            SlotDisplay.CODEC.fieldOf("input_ingredient").forGetter(RecipeDisplayEnvironmentalAccumulator::inputIngredient),
                            WeatherType.CODEC.fieldOf("input_weather").forGetter(RecipeDisplayEnvironmentalAccumulator::inputWeather),
                            SlotDisplay.CODEC.fieldOf("output_ingredient").forGetter(RecipeDisplayEnvironmentalAccumulator::outputItem),
                            WeatherType.CODEC.fieldOf("output_weather").forGetter(RecipeDisplayEnvironmentalAccumulator::outputWeather),
                            SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(RecipeDisplayEnvironmentalAccumulator::craftingStation),
                            Codec.INT.fieldOf("duration").forGetter(RecipeDisplayEnvironmentalAccumulator::duration),
                            Codec.INT.fieldOf("cooldown_time").forGetter(RecipeDisplayEnvironmentalAccumulator::cooldownTime),
                            Codec.FLOAT.fieldOf("processing_speed").forGetter(RecipeDisplayEnvironmentalAccumulator::processingSpeed)
                    )
                    .apply(instance, RecipeDisplayEnvironmentalAccumulator::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, RecipeDisplayEnvironmentalAccumulator> STREAM_CODEC = StreamCodec.composite(
            SlotDisplay.STREAM_CODEC,
            RecipeDisplayEnvironmentalAccumulator::inputIngredient,
            WeatherType.STREAM_CODEC,
            RecipeDisplayEnvironmentalAccumulator::inputWeather,
            SlotDisplay.STREAM_CODEC,
            RecipeDisplayEnvironmentalAccumulator::outputItem,
            WeatherType.STREAM_CODEC,
            RecipeDisplayEnvironmentalAccumulator::outputWeather,
            SlotDisplay.STREAM_CODEC,
            RecipeDisplayEnvironmentalAccumulator::craftingStation,
            ByteBufCodecs.VAR_INT,
            RecipeDisplayEnvironmentalAccumulator::duration,
            ByteBufCodecs.VAR_INT,
            RecipeDisplayEnvironmentalAccumulator::cooldownTime,
            ByteBufCodecs.FLOAT,
            RecipeDisplayEnvironmentalAccumulator::processingSpeed,
            RecipeDisplayEnvironmentalAccumulator::new
    );
    public static final Type<RecipeDisplayEnvironmentalAccumulator> TYPE = new Type<>(MAP_CODEC, STREAM_CODEC);

    @Override
    public SlotDisplay result() {
        return outputItem();
    }

    @Override
    public Type<? extends RecipeDisplay> type() {
        return TYPE;
    }

    @Override
    public boolean isEnabled(FeatureFlagSet featureFlagSet) {
        return this.inputIngredient.isEnabled(featureFlagSet)
                && this.outputItem().isEnabled(featureFlagSet)
                && RecipeDisplay.super.isEnabled(featureFlagSet);
    }
}
