package org.cyclops.evilcraft.core.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

/**
 * @author rubensworks
 */
public class RecipeCraftingShapedCustomOutput extends ShapedRecipe {

    private final IRecipeSerializer<?> serializer;

    public RecipeCraftingShapedCustomOutput(IRecipeSerializer<?> serializer, ResourceLocation idIn, String groupIn, int recipeWidthIn, int recipeHeightIn, NonNullList<Ingredient> recipeItemsIn, ItemStack recipeOutputIn) {
        super(idIn, groupIn, recipeWidthIn, recipeHeightIn, recipeItemsIn, recipeOutputIn);
        this.serializer = serializer;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return this.serializer;
    }
}
