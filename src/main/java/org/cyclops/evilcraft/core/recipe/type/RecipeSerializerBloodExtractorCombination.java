package org.cyclops.evilcraft.core.recipe.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;

/**
 * Recipe serializer for energy container combinations.
 * @author rubensworks
 */
public class RecipeSerializerBloodExtractorCombination implements RecipeSerializer<RecipeBloodExtractorCombination> {

    public static final MapCodec<RecipeBloodExtractorCombination> CODEC = RecordCodecBuilder.mapCodec(
            builder -> builder.group(
                            Codec.INT.fieldOf("max_capacity").forGetter(RecipeBloodExtractorCombination::getMaxCapacity)
                    )
                    .apply(builder, (maxCapacity) -> new RecipeBloodExtractorCombination(CraftingBookCategory.MISC, maxCapacity))
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, RecipeBloodExtractorCombination> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, RecipeBloodExtractorCombination::getMaxCapacity,
            (maxCapacity) -> new RecipeBloodExtractorCombination(CraftingBookCategory.MISC, maxCapacity)
    );

    @Override
    public MapCodec<RecipeBloodExtractorCombination> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, RecipeBloodExtractorCombination> streamCodec() {
        return STREAM_CODEC;
    }
}
