package evilcraft.core.recipe.custom;

import evilcraft.api.recipes.custom.IMachine;
import evilcraft.api.recipes.custom.IRecipe;
import evilcraft.api.recipes.custom.IRecipeMatch;
import lombok.Data;

/**
 * A match for a recipe, holds the recipe and the machine it is associated with.
 * @param <M> The type of the machine.
 * @param <R> The type of the recipe.
 */
@Data
public class RecipeMatch<M extends IMachine, R extends IRecipe> implements IRecipeMatch<M, R> {
    private final M machine;
    private final R recipe;
}