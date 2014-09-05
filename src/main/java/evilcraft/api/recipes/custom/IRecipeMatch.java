package evilcraft.api.recipes.custom;

/**
 * A match for a recipe, holds the recipe and the machine it is associated with.
 * @param <M> The type of the machine.
 * @param <R> The type of the recipe.
 */
@SuppressWarnings("rawtypes")
public interface IRecipeMatch<M extends IMachine, R extends IRecipe> {

	/**
	 * @return The machine.
	 */
	public M getMachine();
	/**
	 * @return The recipe.
	 */
	public R getRecipe();
	
}
