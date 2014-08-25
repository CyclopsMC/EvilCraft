package evilcraft.api.recipes;

/**
 * Interface for machines that can be registered with {@link SuperRecipeRegistry}.
 *
 * @author immortaleeb
 */
public interface IMachine<M extends IMachine<M, I, O, P>, I extends IRecipeInput, O extends IRecipeOutput, P extends IRecipeProperties> {
    /**
     * @return Returns the recipe registry responsible for registring recipes for the current machine.
     */
    public SuperRecipeRegistry.RecipeRegistry<M, I, O, P> getRecipeRegistry();
}
