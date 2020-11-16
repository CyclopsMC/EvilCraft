package org.cyclops.evilcraft.core.recipe.type;

import com.google.gson.JsonObject;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

/**
 * Recipe serializer for energy container combinations.
 * @author rubensworks
 */
public class RecipeSerializerBloodExtractorCombination extends ForgeRegistryEntry<IRecipeSerializer<?>>
        implements IRecipeSerializer<RecipeBloodExtractorCombination> {

    @Override
    public RecipeBloodExtractorCombination read(ResourceLocation recipeId, JsonObject json) {
        int maxCapacity = JSONUtils.getInt(json, "maxCapacity");
        return new RecipeBloodExtractorCombination(recipeId, maxCapacity);
    }

    @Nullable
    @Override
    public RecipeBloodExtractorCombination read(ResourceLocation recipeId, PacketBuffer buffer) {
        int maxCapacity = buffer.readInt();
        return new RecipeBloodExtractorCombination(recipeId, maxCapacity);
    }

    @Override
    public void write(PacketBuffer buffer, RecipeBloodExtractorCombination recipe) {
        buffer.writeInt(recipe.getMaxCapacity());
    }
}
