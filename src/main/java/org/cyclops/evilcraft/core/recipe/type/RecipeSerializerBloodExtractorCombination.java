package org.cyclops.evilcraft.core.recipe.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;

import javax.annotation.Nullable;

/**
 * Recipe serializer for energy container combinations.
 * @author rubensworks
 */
public class RecipeSerializerBloodExtractorCombination implements RecipeSerializer<RecipeBloodExtractorCombination> {

    public static final Codec<RecipeBloodExtractorCombination> CODEC = RecordCodecBuilder.create(
            builder -> builder.group(
                            Codec.INT.fieldOf("max_capacity").forGetter(RecipeBloodExtractorCombination::getMaxCapacity)
                    )
                    .apply(builder, (maxCapacity) -> new RecipeBloodExtractorCombination(CraftingBookCategory.MISC, maxCapacity))
    );

    @Override
    public Codec<RecipeBloodExtractorCombination> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public RecipeBloodExtractorCombination fromNetwork(FriendlyByteBuf buffer) {
        int maxCapacity = buffer.readInt();
        return new RecipeBloodExtractorCombination(CraftingBookCategory.MISC, maxCapacity);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, RecipeBloodExtractorCombination recipe) {
        buffer.writeInt(recipe.getMaxCapacity());
    }
}
