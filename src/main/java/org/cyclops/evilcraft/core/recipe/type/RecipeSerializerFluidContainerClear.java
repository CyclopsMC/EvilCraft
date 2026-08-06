package org.cyclops.evilcraft.core.recipe.type;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

/**
 * Recipe serializer for fluid container clear recipes.
 * @author rubensworks
 */
public class RecipeSerializerFluidContainerClear {

    public static final MapCodec<RecipeFluidContainerClear> CODEC = RecordCodecBuilder.mapCodec(
            builder -> builder.group(
                            Ingredient.CODEC.fieldOf("item").forGetter(RecipeFluidContainerClear::getInputIngredient)
                    )
                    .apply(builder, RecipeFluidContainerClear::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, RecipeFluidContainerClear> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, RecipeFluidContainerClear::getInputIngredient,
            RecipeFluidContainerClear::new
    );

    public static final RecipeSerializer<RecipeFluidContainerClear> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);
}
