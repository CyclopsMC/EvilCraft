package evilcraft.api.recipes.custom;

import java.util.List;

/**
 * This class allows you to register and search for recipes that are associated with a specific machine.
 * In order to return an instance of this class use {@link evilcraft.api.recipes.custom.ISuperRecipeRegistry#getRecipeRegistry(IMachine)}
 * by passing the appropriate machine.
 * @param <M> The type of the machine.
 * @param <I> The type of the recipe input of all recipes associated with the machine.
 * @param <O> The type of the recipe output of all recipes associated with the machine.
 * @param <P> The type of the recipe properties of all recipes associated with the machine.
 */
public interface IRecipeRegistry<M extends IMachine<M, I, O, P>, I extends IRecipeInput, O extends IRecipeOutput, P extends IRecipeProperties> {

	/**
     * Registers the given recipe with this registry.
     * @param recipe A recipe that should be registered.
     */
    public void registerRecipe(IRecipe<I, O, P> recipe);
    
    /**
     * Registers a new recipe with the given properties with this registry.
     * @param namedId A unique name for the given recipe.
     * @param input The input of the recipe.
     * @param output The output of the recipe.
     * @param properties Additional properties of the recipe.
     */
    public void registerRecipe(String namedId, I input, O output, P properties);
    
    /**
     * Registers a new recipe with the given properties with this registry.
     * @param input The input of the recipe.
     * @param output The output of the recipe.
     * @param properties Additional properties of the recipe.
     */
    public void registerRecipe(I input, O output, P properties);
    
    /**
     * Returns the first recipe whose named id matches the given named id.
     * @param namedId The unique named id of the recipe.
     * @return The first recipe whose named id matches, or null if no match was found.
     */
    public IRecipe<I, O, P> findRecipeByNamedId(String namedId);
    
    /**
     * Returns the first recipe whose input matches the given recipe input.
     * @param input The input of the recipe.
     * @return The first recipe whose input matches, or null if no match was found.
     */
    public IRecipe<I, O, P> findRecipeByInput(I input);
    
    /**
     * Returns a list of recipes whose input match the given recipe input.
     * @param input The input of the recipe.
     * @return A list of recipes whose recipe input match the given input.
     */
    public List<IRecipe<I, O, P>> findRecipesByInput(I input);
    
    /**
     * Returns the first recipe whose output matches the given recipe output.
     * @param output The output of the recipe.
     * @return The first recipe whose output matches, or null if no match was found.
     */
    public IRecipe<I, O, P> findRecipeByOutput(O output);
    
    /**
     * Returns a list of recipes whose output match the given recipe output.
     * @param output The output of the recipe.
     * @return A list of recipes whose recipe output match the given output.
     */
    public List<IRecipe<I, O, P>> findRecipesByOutput(O output);
    
    /**
     * Returns the first recipe for which the matches() method of the given RecipeMatcher returns true.
     * @param recipeMatcher Defines the criteria for finding recipes.
     * @return The first matched recipe, or null if no such recipe is found.
     */
    public IRecipe<I, O, P> findRecipe(IRecipeMatcher<M, IRecipe<I, O, P>> recipeMatcher);
    
    /**
     * Returns a list of recipes for which the matches() method of the given RecipeMatcher return true.
     * @param recipeMatcher Defines the criteria for finding recipes.
     * @return A list of recipes that match the given criteria.
     */
    public List<IRecipe<I, O, P>> findRecipes(IRecipeMatcher<M, IRecipe<I, O, P>> recipeMatcher);
    
    /**
     * Returns a list of all recipes that are associated with the current machine.
     * @return A list of all recipes associated with the current machine.
     */
    public List<IRecipe<I, O, P>> allRecipes();
    
}
