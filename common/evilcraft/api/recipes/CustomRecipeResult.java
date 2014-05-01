package evilcraft.api.recipes;

import net.minecraft.item.ItemStack;

/**
 * The result of a {@link CustomRecipe} that is stored in {@link CustomRecipeRegistry}.
 * There is no need to manually create instances of this, they will automatically be created
 * in {@link CustomRecipeRegistry} when you add a new mapping.
 * @see CustomRecipe
 * @see CustomRecipeRegistry
 * @author rubensworks
 *
 */
public class CustomRecipeResult {
    private CustomRecipe recipe;
    private ItemStack result;
    
    /**
     * Create a new result for a {@link CustomRecipe}.
     * Note: These do not have to be created manually, this is done by
     * the {@link CustomRecipeRegistry}.
     * @param recipe The minimal Custom Recipe.
     * @param result The resulting itemStack for the given recipe.
     */
    public CustomRecipeResult(CustomRecipe recipe, ItemStack result) {
        this.recipe = recipe;
        this.result = result;
    }
    
    /**
     * Get the minimal recipe for this result.
     * @return The minimal recipe.
     */
    public CustomRecipe getRecipe() {
        return recipe;
    }
    
    /**
     * Get the resulting itemStack.
     * @return The resulting itemStack.
     */
    public ItemStack getResult() {
        return result;
    }
    
}
