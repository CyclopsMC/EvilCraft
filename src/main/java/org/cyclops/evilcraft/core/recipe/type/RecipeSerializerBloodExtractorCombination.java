package org.cyclops.evilcraft.core.recipe.type;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;

import javax.annotation.Nullable;

/**
 * Recipe serializer for energy container combinations.
 * @author rubensworks
 */
public class RecipeSerializerBloodExtractorCombination implements RecipeSerializer<RecipeBloodExtractorCombination> {

    @Override
    public RecipeBloodExtractorCombination fromJson(ResourceLocation recipeId, JsonObject json) {
        int maxCapacity = GsonHelper.getAsInt(json, "maxCapacity");
        return new RecipeBloodExtractorCombination(recipeId, CraftingBookCategory.MISC, maxCapacity);
    }

    @Nullable
    @Override
    public RecipeBloodExtractorCombination fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        int maxCapacity = buffer.readInt();
        return new RecipeBloodExtractorCombination(recipeId, CraftingBookCategory.MISC, maxCapacity);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, RecipeBloodExtractorCombination recipe) {
        buffer.writeInt(recipe.getMaxCapacity());
    }
}
