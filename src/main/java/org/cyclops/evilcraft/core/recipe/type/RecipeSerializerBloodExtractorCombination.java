package org.cyclops.evilcraft.core.recipe.type;

import com.google.gson.JsonObject;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

/**
 * Recipe serializer for energy container combinations.
 * @author rubensworks
 */
public class RecipeSerializerBloodExtractorCombination extends ForgeRegistryEntry<RecipeSerializer<?>>
        implements RecipeSerializer<RecipeBloodExtractorCombination> {

    @Override
    public RecipeBloodExtractorCombination fromJson(ResourceLocation recipeId, JsonObject json) {
        int maxCapacity = GsonHelper.getAsInt(json, "maxCapacity");
        return new RecipeBloodExtractorCombination(recipeId, maxCapacity);
    }

    @Nullable
    @Override
    public RecipeBloodExtractorCombination fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        int maxCapacity = buffer.readInt();
        return new RecipeBloodExtractorCombination(recipeId, maxCapacity);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, RecipeBloodExtractorCombination recipe) {
        buffer.writeInt(recipe.getMaxCapacity());
    }
}
