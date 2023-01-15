package org.cyclops.evilcraft.core.recipe.type;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.cyclops.cyclopscore.helper.RecipeSerializerHelpers;

import javax.annotation.Nullable;

/**
 * Recipe serializer for energy container combinations.
 * @author rubensworks
 */
public class RecipeSerializerFluidContainerCombination implements RecipeSerializer<RecipeFluidContainerCombination> {

    @Override
    public RecipeFluidContainerCombination fromJson(ResourceLocation recipeId, JsonObject json) {
        Ingredient inputIngredient = RecipeSerializerHelpers.getJsonIngredient(json, "item", false);
        int maxCapacity = GsonHelper.getAsInt(json, "maxCapacity");
        return new RecipeFluidContainerCombination(recipeId, CraftingBookCategory.MISC, inputIngredient, maxCapacity);
    }

    @Nullable
    @Override
    public RecipeFluidContainerCombination fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        Ingredient inputIngredient = Ingredient.fromNetwork(buffer);
        int maxCapacity = buffer.readInt();
        return new RecipeFluidContainerCombination(recipeId, CraftingBookCategory.MISC, inputIngredient, maxCapacity);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, RecipeFluidContainerCombination recipe) {
        recipe.getFluidContainer().toNetwork(buffer);
        buffer.writeInt(recipe.getMaxCapacity());
    }
}
