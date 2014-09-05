package evilcraft.core.recipes;

/**
 * Interface for recipes that can be registered with {@link SuperRecipeRegistry}.
 *
 * @author immortaleeb
 */
public interface IRecipe<I extends IRecipeInput, O extends IRecipeOutput, P extends IRecipeProperties> {
    /**
     * Returns the input for this recipe.
     * @return The input for this recipe.
     */
    public I getInput();

    /**
     * Returns additional properties for this recipe that do not belong to the input or output.
     * Example could be the processing duration, power requirements, etc...
     * @return Additional properties for this recipe.
     */
    public P getProperties();

    /**
     * Returns the output for this recipe.
     * @return Returns the output for this recipe.
     */
    public O getOutput();

    /**
     * Returns a unique name for this recipe.
     * @return The unique name for this recipe.
     */
    public String getNamedId();
}
