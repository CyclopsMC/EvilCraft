package org.cyclops.evilcraft.core.recipe.type;

import com.google.gson.JsonObject;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.cyclops.cyclopscore.helper.RecipeSerializerHelpers;

import javax.annotation.Nullable;

/**
 * Recipe serializer for energy container combinations.
 * @author rubensworks
 */
public class RecipeSerializerFluidContainerCombination extends ForgeRegistryEntry<IRecipeSerializer<?>>
        implements IRecipeSerializer<RecipeFluidContainerCombination> {

    @Override
    public RecipeFluidContainerCombination fromJson(ResourceLocation recipeId, JsonObject json) {
        Ingredient inputIngredient = RecipeSerializerHelpers.getJsonIngredient(json, "item", false);
        int maxCapacity = JSONUtils.getAsInt(json, "maxCapacity");
        return new RecipeFluidContainerCombination(recipeId, inputIngredient, maxCapacity);
    }

    @Nullable
    @Override
    public RecipeFluidContainerCombination fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
        Ingredient inputIngredient = Ingredient.fromNetwork(buffer);
        int maxCapacity = buffer.readInt();
        return new RecipeFluidContainerCombination(recipeId, inputIngredient, maxCapacity);
    }

    @Override
    public void toNetwork(PacketBuffer buffer, RecipeFluidContainerCombination recipe) {
        recipe.getFluidContainer().toNetwork(buffer);
        buffer.writeInt(recipe.getMaxCapacity());
    }
}
