package evilcraft.core.recipe.custom;

import evilcraft.api.recipes.custom.IMachine;
import evilcraft.api.recipes.custom.IRecipe;

/**
 * It allows you to match a property of a recipe to the properties of all other registered recipes and return
 * any recipes whose properties match. You do this by implementing the getProperty() method, which should return
 * the property (of type T) of the given recipe that should be matched with the property given to the constructor.
 * @param <P> The class of the property that should be matched.
 */
public abstract class RecipePropertyMatcher<M extends IMachine, R extends IRecipe, P> extends RecipeMatcher<M, R> {
    private final P property;

    public RecipePropertyMatcher(P property) {
        this.property = property;
    }

    @Override
    public boolean matches(M machine, R recipeToMatch) {
        P recipeProperty = getProperty(machine, recipeToMatch);
        return recipeProperty != null && recipeProperty.equals(property);
    }

    /**
     * Returns the property of the recipe that should be matched.
     * @param recipe A recipe whose properties should be matched.
     * @return The property of the given recipe that should be matched.
     */
    public abstract P getProperty(M machine, R recipe);
}