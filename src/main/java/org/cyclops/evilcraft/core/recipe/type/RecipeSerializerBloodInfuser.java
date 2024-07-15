package org.cyclops.evilcraft.core.recipe.type;

import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.fluids.FluidStack;
import org.cyclops.cyclopscore.helper.RecipeSerializerHelpers;
import org.cyclops.evilcraft.blockentity.BlockEntityBloodInfuserConfig;

/**
 * Recipe serializer for blood infuser recipes
 * @author rubensworks
 */
public class RecipeSerializerBloodInfuser implements RecipeSerializer<RecipeBloodInfuser> {

    public static final MapCodec<RecipeBloodInfuser> CODEC = RecordCodecBuilder.mapCodec(
            builder -> builder.group(
                            Ingredient.CODEC_NONEMPTY.optionalFieldOf("input_item").forGetter(RecipeBloodInfuser::getInputIngredient),
                            FluidStack.CODEC.optionalFieldOf("input_fluid").forGetter(RecipeBloodInfuser::getInputFluid),
                            Codec.INT.optionalFieldOf("tier").forGetter(RecipeBloodInfuser::getInputTier),
                            RecipeSerializerHelpers.getCodecItemStackOrTag(() -> BlockEntityBloodInfuserConfig.recipeTagOutputModPriorities).fieldOf("output_item").forGetter(RecipeBloodInfuser::getOutputItem),
                            Codec.INT.fieldOf("duration").forGetter(RecipeBloodInfuser::getDuration),
                            Codec.FLOAT.optionalFieldOf("xp").forGetter(RecipeBloodInfuser::getXp)
                    )
                    .apply(builder, (inputIngredient, inputFluid, inputTier, outputItemStack, duration, xp) -> {
                        // Validation
                        if (inputIngredient.isEmpty() && inputFluid.isEmpty()) {
                            throw new JsonSyntaxException("An input item or fluid is required");
                        }
                        if (inputTier.isPresent() && inputTier.get() < 0) {
                            throw new JsonSyntaxException("Tiers can not be negative");
                        }
                        if (duration <= 0) {
                            throw new JsonSyntaxException("Durations must be higher than one tick");
                        }
                        if (xp.isPresent() && xp.get() < 0) {
                            throw new JsonSyntaxException("XP can not be negative");
                        }

                        return new RecipeBloodInfuser(inputIngredient, inputFluid, inputTier, outputItemStack, duration, xp);
                    })
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, RecipeBloodInfuser> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(Ingredient.CONTENTS_STREAM_CODEC), RecipeBloodInfuser::getInputIngredient,
            ByteBufCodecs.optional(FluidStack.STREAM_CODEC), RecipeBloodInfuser::getInputFluid,
            ByteBufCodecs.optional(ByteBufCodecs.INT), RecipeBloodInfuser::getInputTier,
            RecipeSerializerHelpers.STREAM_CODEC_ITEMSTACK_OR_TAG, RecipeBloodInfuser::getOutputItem,
            ByteBufCodecs.INT, RecipeBloodInfuser::getDuration,
            ByteBufCodecs.optional(ByteBufCodecs.FLOAT), RecipeBloodInfuser::getXp,
            RecipeBloodInfuser::new
    );

    @Override
    public MapCodec<RecipeBloodInfuser> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, RecipeBloodInfuser> streamCodec() {
        return STREAM_CODEC;
    }
}
