package evilcraft.core.recipe.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import evilcraft.core.recipe.xml.XmlRecipeLoader.XmlRecipeException;

/**
 * Recipe type handler for super recipes.
 * @author rubensworks
 */
public abstract class SuperRecipeTypeHandler extends CommonRecipeTypeHandler {

	@Override
	public void loadRecipe(Node recipe) {
		Element recipeElement = (Element) recipe;
		Element input = (Element) recipeElement.getElementsByTagName("input").item(0);
		Element output = (Element) recipeElement.getElementsByTagName("output").item(0);
		Element properties = (Element) recipeElement.getElementsByTagName("properties").item(0);
		handleRecipe(input, output, properties);
	}
	
	protected abstract void handleRecipe(Element input, Element output, Element properties)
			throws XmlRecipeException;

}
