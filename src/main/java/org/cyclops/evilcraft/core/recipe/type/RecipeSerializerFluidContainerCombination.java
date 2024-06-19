package org.cyclops.evilcraft.core.recipe.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import javax.annotation.Nullable;

/**
 * Recipe serializer for energy container combinations.
 * @author rubensworks
 */
public class RecipeSerializerFluidContainerCombination implements RecipeSerializer<RecipeFluidContainerCombination> {

    public static final Codec<RecipeFluidContainerCombination> CODEC = RecordCodecBuilder.create(
            builder -> builder.group(
                            Ingredient.CODEC_NONEMPTY.fieldOf("item").forGetter(RecipeFluidContainerCombination::getFluidContainer),
                            Codec.INT.fieldOf("max_capacity").forGetter(RecipeFluidContainerCombination::getMaxCapacity)
                    )
                    .apply(builder, (inputIngredient, maxCapacity) -> new RecipeFluidContainerCombination(CraftingBookCategory.MISC, inputIngredient, maxCapacity))
    );

    @Override
    public Codec<RecipeFluidContainerCombination> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public RecipeFluidContainerCombination fromNetwork(FriendlyByteBuf buffer) {
        Ingredient inputIngredient = Ingredient.fromNetwork(buffer);
        int maxCapacity = buffer.readInt();
        return new RecipeFluidContainerCombination(CraftingBookCategory.MISC, inputIngredient, maxCapacity);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, RecipeFluidContainerCombination recipe) {
        recipe.getFluidContainer().toNetwork(buffer);
        buffer.writeInt(recipe.getMaxCapacity());
    }
}
