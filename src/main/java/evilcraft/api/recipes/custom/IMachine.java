package evilcraft.api.recipes.custom;


/**
 * Interface for machines that can be registered with {@link ISuperRecipeRegistry}.
 *
 * @author immortaleeb
 * @param <M> The type of the machine.
 * @param <I> The type of the recipe input of all recipes associated with the machine.
 * @param <O> The type of the recipe output of all recipes associated with the machine.
 * @param <P> The type of the recipe properties of all recipes associated with the machine.
 */
public interface IMachine<M extends IMachine<M, I, O, P>, I extends IRecipeInput, O extends IRecipeOutput, P extends IRecipeProperties> {
    /**
     * @return Returns the recipe registry responsible for registring recipes for the current machine.
     */
    public IRecipeRegistry<M, I, O, P> getRecipeRegistry();
}
