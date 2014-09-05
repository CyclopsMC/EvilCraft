package evilcraft.core.recipes.xml;

/**
 * Handler to check a type of recipe condition.
 * @author rubensworks
 *
 */
public interface IRecipeConditionHandler {

	/**
	 * Check if this condition is satisfied for the given parameter.
	 * @param param The condition parameter.
	 * @return If this condition is satisfied.
	 */
	public boolean isSatisfied(String param);
	
}
