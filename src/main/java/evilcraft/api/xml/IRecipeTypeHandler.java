package evilcraft.api.xml;

import org.w3c.dom.Node;

/**
 * Handler for a specific type of recipe.
 * @author rubensworks
 */
public interface IRecipeTypeHandler {

	/**
	 * Load the given recipe stored in an xml node for this recipe type.
	 * @param recipe The recipe node.
	 */
	public void loadRecipe(Node recipe);
	
}
