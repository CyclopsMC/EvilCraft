package evilcraft.api.recipes.custom;

import org.cyclops.cyclopscore.init.IRegistry;

import java.util.List;

/**
 * Machine recipe registry manager.
 * @author rubensworks
 */
public interface ISuperRecipeRegistry extends IRegistry {

	/**
     * Returns the registry that is responsible for all recipes of the given machine.
     * If no recipe exists when calling this function, it will explicitly be created.
     *
     * @param machine The machine for which we want the registry.
     * @param <M> The type of the machine.
     * @param <I> The type of input of all recipes for this machine.
     * @param <O> The type of ouput of all recipes for this machine.
     * @param <P> The type of properties of all recipes for this machine.
     * @return The registry responsible for all recipes of the given machine.
     */
    public <M extends IMachine<M, I, O, P>, I extends IRecipeInput, O extends IRecipeOutput, P extends IRecipeProperties>
        IRecipeRegistry<M, I, O, P> getRecipeRegistry(M machine);
    
    /**
     * Get the recipes for a machine.
     * @param machine The machine.
     * @return The recipes.
     */
    @SuppressWarnings("rawtypes")
    public List<IRecipe> getRecipes(IMachine machine);
    
    /**
     * Finds all recipes who have the given named id.
     * @param namedId The unique named id of the recipe.
     * @return Returns the recipe whose named id matches the given named id.
     */
    @SuppressWarnings("rawtypes")
    public IRecipeMatch<IMachine, IRecipe> findRecipeByNamedId(String namedId);
    
    /**
     * Finds all recipes who have the given recipe input.
     * @param input The input of the recipe.
     * @return Returns a list of recipes whose input equal the recipe input that was given.
     */
    @SuppressWarnings("rawtypes")
    public List<IRecipeMatch<IMachine, IRecipe>> findRecipesByInput(IRecipeInput input);
    
    /**
     * Returns all the recipes for the given machine.
     * @param machine The machine for which the recipes should be returned.
     * @return A list of recipes that the given machine can process.
     */
    @SuppressWarnings("rawtypes")
    public List<IRecipeMatch<IMachine, IRecipe>> findRecipesByMachine(IMachine machine);
    
    /**
     * Returns the first recipe for which the matches() method of the given
     * {@link IRecipeMatcher} returns true.
     * @param recipeMatcher A matcher that defines which recipes fit some matching criteria.
     * @param <M> The machine type.
     * @param <R> The recipe type.
     * @return The first recipe which matches the criteria, or null in case no such recipe was found.
     */
    @SuppressWarnings("rawtypes")
	public <M extends IMachine, R extends IRecipe> IRecipeMatch<M, R> findRecipe(IRecipeMatcher<M, R> recipeMatcher);
    
    /**
     * Returns a list of registered recipe for which the matches() method of the given
     * {@link IRecipeMatcher} returns true.
     * @param recipeMatcher A matcher that defines which recipes fit some matching criteria.
     * @param <M> The machine type.
     * @param <R> The recipe type.
     * @return A list of recipes that match the given criteria.
     */
    @SuppressWarnings("rawtypes")
    public <M extends IMachine, R extends IRecipe> List<IRecipeMatch<M, R>> findRecipes(IRecipeMatcher<M, R> recipeMatcher);
	
}
