package org.cyclops.evilcraft.core.recipe.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

/**
 * Recipe serializer for energy container combinations.
 * @author rubensworks
 */
public class RecipeSerializerFluidContainerCombination implements RecipeSerializer<RecipeFluidContainerCombination> {

    public static final MapCodec<RecipeFluidContainerCombination> CODEC = RecordCodecBuilder.mapCodec(
            builder -> builder.group(
                            Ingredient.CODEC_NONEMPTY.fieldOf("item").forGetter(RecipeFluidContainerCombination::getFluidContainer),
                            Codec.INT.fieldOf("max_capacity").forGetter(RecipeFluidContainerCombination::getMaxCapacity)
                    )
                    .apply(builder, (inputIngredient, maxCapacity) -> new RecipeFluidContainerCombination(CraftingBookCategory.MISC, inputIngredient, maxCapacity))
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, RecipeFluidContainerCombination> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, RecipeFluidContainerCombination::getFluidContainer,
            ByteBufCodecs.INT, RecipeFluidContainerCombination::getMaxCapacity,
            (inputIngredient, maxCapacity) -> new RecipeFluidContainerCombination(CraftingBookCategory.MISC, inputIngredient, maxCapacity)
    );

    @Override
    public MapCodec<RecipeFluidContainerCombination> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, RecipeFluidContainerCombination> streamCodec() {
        return STREAM_CODEC;
    }
}
