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
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * @author rubensworks
 */
public record RecipeDisplayBloodInfuser(
        SlotDisplay inputIngredient,
        FluidStack inputFluid,
        int inputTier,
        SlotDisplay outputItem,
        SlotDisplay craftingStation,
        int duration,
        float xp
) implements RecipeDisplay {

    public static final MapCodec<RecipeDisplayBloodInfuser> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            SlotDisplay.CODEC.fieldOf("input_ingredient").forGetter(RecipeDisplayBloodInfuser::inputIngredient),
                            FluidStack.CODEC.fieldOf("input_fluid").forGetter(RecipeDisplayBloodInfuser::inputFluid),
                            Codec.INT.fieldOf("tier").forGetter(RecipeDisplayBloodInfuser::inputTier),
                            SlotDisplay.CODEC.fieldOf("output_ingredient").forGetter(RecipeDisplayBloodInfuser::outputItem),
                            SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(RecipeDisplayBloodInfuser::craftingStation),
                            Codec.INT.fieldOf("duration").forGetter(RecipeDisplayBloodInfuser::duration),
                            Codec.FLOAT.fieldOf("xp").forGetter(RecipeDisplayBloodInfuser::xp)
                    )
                    .apply(instance, RecipeDisplayBloodInfuser::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, RecipeDisplayBloodInfuser> STREAM_CODEC = StreamCodec.composite(
            SlotDisplay.STREAM_CODEC,
            RecipeDisplayBloodInfuser::inputIngredient,
            FluidStack.OPTIONAL_STREAM_CODEC,
            RecipeDisplayBloodInfuser::inputFluid,
            ByteBufCodecs.VAR_INT,
            RecipeDisplayBloodInfuser::inputTier,
            SlotDisplay.STREAM_CODEC,
            RecipeDisplayBloodInfuser::outputItem,
            SlotDisplay.STREAM_CODEC,
            RecipeDisplayBloodInfuser::craftingStation,
            ByteBufCodecs.VAR_INT,
            RecipeDisplayBloodInfuser::duration,
            ByteBufCodecs.FLOAT,
            RecipeDisplayBloodInfuser::xp,
            RecipeDisplayBloodInfuser::new
    );
    public static final Type<RecipeDisplayBloodInfuser> TYPE = new Type<>(MAP_CODEC, STREAM_CODEC);

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
