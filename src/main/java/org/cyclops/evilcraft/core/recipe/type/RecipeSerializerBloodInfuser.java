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
import net.neoforged.neoforge.fluids.FluidStack;
import org.cyclops.cyclopscore.helper.RecipeSerializerHelpers;
import org.cyclops.cyclopscore.recipe.ItemStackFromIngredient;
import org.cyclops.evilcraft.blockentity.BlockEntityBloodInfuserConfig;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Recipe serializer for blood infuser recipes
 * @author rubensworks
 */
public class RecipeSerializerBloodInfuser implements RecipeSerializer<RecipeBloodInfuser> {

    public static final Codec<RecipeBloodInfuser> CODEC = RecordCodecBuilder.create(
            builder -> builder.group(
                            ExtraCodecs.strictOptionalField(Ingredient.CODEC_NONEMPTY, "input_item").forGetter(RecipeBloodInfuser::getInputIngredient),
                            ExtraCodecs.strictOptionalField(FluidStack.CODEC, "input_fluid").forGetter(RecipeBloodInfuser::getInputFluid),
                            ExtraCodecs.strictOptionalField(Codec.INT, "tier").forGetter(RecipeBloodInfuser::getInputTier),
                            RecipeSerializerHelpers.getCodecItemStackOrTag(() -> BlockEntityBloodInfuserConfig.recipeTagOutputModPriorities).fieldOf("output_item").forGetter(RecipeBloodInfuser::getOutputItem),
                            Codec.INT.fieldOf("duration").forGetter(RecipeBloodInfuser::getDuration),
                            ExtraCodecs.strictOptionalField(Codec.FLOAT, "xp").forGetter(RecipeBloodInfuser::getXp)
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

    @Override
    public Codec<RecipeBloodInfuser> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public RecipeBloodInfuser fromNetwork(FriendlyByteBuf buffer) {
        // Input
        Optional<Ingredient> inputIngredient = RecipeSerializerHelpers.readOptionalFromNetwork(buffer, Ingredient::fromNetwork);
        Optional<FluidStack> inputFluid = RecipeSerializerHelpers.readOptionalFromNetwork(buffer, FluidStack::readFromPacket);
        Optional<Integer> inputTier = RecipeSerializerHelpers.readOptionalFromNetwork(buffer, FriendlyByteBuf::readVarInt);

        // Output
        Either<ItemStack, ItemStackFromIngredient> outputItem = RecipeSerializerHelpers.readItemStackOrItemStackIngredient(buffer);

        // Other stuff
        int duration = buffer.readVarInt();
        Optional<Float> xp = RecipeSerializerHelpers.readOptionalFromNetwork(buffer, FriendlyByteBuf::readFloat);

        return new RecipeBloodInfuser(inputIngredient, inputFluid, inputTier, outputItem, duration, xp);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, RecipeBloodInfuser recipe) {
        // Input
        RecipeSerializerHelpers.writeOptionalToNetwork(buffer, recipe.getInputIngredient(), (b, value) -> value.toNetwork(b));
        RecipeSerializerHelpers.writeOptionalToNetwork(buffer, recipe.getInputFluid(), (b, value) -> value.writeToPacket(b));
        RecipeSerializerHelpers.writeOptionalToNetwork(buffer, recipe.getInputTier(), FriendlyByteBuf::writeVarInt);

        // Output
        RecipeSerializerHelpers.writeItemStackOrItemStackIngredient(buffer, recipe.getOutputItem());

        // Other stuff
        buffer.writeVarInt(recipe.getDuration());
        RecipeSerializerHelpers.writeOptionalToNetwork(buffer, recipe.getXp(), FriendlyByteBuf::writeFloat);
    }
}
