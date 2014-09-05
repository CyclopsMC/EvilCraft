package evilcraft.core.recipe.custom;

import java.util.List;

import evilcraft.api.RegistryManager;
import evilcraft.api.recipes.custom.IMachine;
import evilcraft.api.recipes.custom.IRecipe;
import evilcraft.api.recipes.custom.IRecipeMatch;
import evilcraft.api.recipes.custom.IRecipeMatcher;
import evilcraft.api.recipes.custom.ISuperRecipeRegistry;

/**
 * Helper class to abstract away some function logic (lambdas would make this way simpler though).
 * It allows you to define a matching criteria and allows you to filter out recipe that match these criteria
 * and those that do not. You do this by implementing the matches() method, which should return true when
 * the criteria of the given recipe are met, or false otherwise.
 */
public abstract class RecipeMatcher<M extends IMachine, R extends IRecipe> 
	implements IRecipeMatcher<M, R> {

    @Override
	public IRecipeMatch<M, R> findRecipe() {
        return RegistryManager.getRegistry(ISuperRecipeRegistry.class).findRecipe(this);
    }

    @Override
	public List<IRecipeMatch<M, R>> findRecipes() {
        return (RegistryManager.getRegistry(ISuperRecipeRegistry.class).findRecipes(this));
    }
}