package evilcraft;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

/**
 * A registry for mapping {@link CustomRecipe} to {@link CustomRecipeResult}.
 * @see CustomRecipe
 * @see CustomRecipeResult
 * @author rubensworks
 *
 */
public class CustomRecipeRegistry {
    
    private static final Map<CustomRecipe, CustomRecipeResult> recipes = new HashMap<CustomRecipe, CustomRecipeResult>();
    
    /**
     * Add a new recipe and corresponding result to the registry map.
     * @param recipe The input recipe.
     * @param result The resulting itemStack that acts as output of the recipe.
     */
    public static void put(CustomRecipe recipe, ItemStack result) {
        recipes.put(recipe, new CustomRecipeResult(recipe, result));
    }
    
    /**
     * Get the result of a given recipe.
     * @param recipe The minimal recipe. @see {@link CustomRecipe}
     * @return The result of the given minimal recipe. Will be null if none can be found.
     */
    public static CustomRecipeResult get(CustomRecipe recipe) {
        // TODO: when the recipes list gets big, this might need to get more efficient...
        for(Entry<CustomRecipe, CustomRecipeResult> entry : recipes.entrySet()) {
            if(entry.getKey().equals(recipe))
                return entry.getValue();
        }
        return null;
    }
    
    /**
     * Get the recipe of a given result.
     * @param result The resulting {@link ItemStack}.
     * @return The recipe of the given result. Will be null if none can be found.
     */
    public static CustomRecipe get(ItemStack result) {
        // TODO: when the recipes list gets big, this might need to get more efficient...
        for(Entry<CustomRecipe, CustomRecipeResult> entry : recipes.entrySet()) {
            if(ItemStack.areItemStacksEqual(entry.getValue().getResult(), result))
                return entry.getKey();
        }
        return null;
    }
    
    /**
     * Retrieve the map of all recipes and their results for a given factory.
     * @param factory The factory block to search for.
     * @return The Map of {@link CustomRecipe} to {@link CustomRecipeResult}.
     */
    public static Map<CustomRecipe, CustomRecipeResult> getRecipesForFactory(Block factory) {
        Map<CustomRecipe, CustomRecipeResult> factoryRecipes = new HashMap<CustomRecipe, CustomRecipeResult>();
        for(Entry<CustomRecipe, CustomRecipeResult> entry : recipes.entrySet()) {
            if(entry.getKey().getFactory() == factory)
                factoryRecipes.put(entry.getKey(), entry.getValue());
        }
        return factoryRecipes;
    }
}
