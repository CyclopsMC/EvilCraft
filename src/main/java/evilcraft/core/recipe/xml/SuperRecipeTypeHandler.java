package evilcraft.core.recipe.xml;

import evilcraft.core.recipe.xml.XmlRecipeLoader.XmlRecipeException;
import net.minecraft.item.ItemStack;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Recipe type handler for super recipes.
 * @author rubensworks
 */
public abstract class SuperRecipeTypeHandler extends CommonRecipeTypeHandler {

	@Override
	public ItemStack loadRecipe(Node recipe) {
		Element recipeElement = (Element) recipe;
		Element input = (Element) recipeElement.getElementsByTagName("input").item(0);
		Element output = (Element) recipeElement.getElementsByTagName("output").item(0);
		Element properties = (Element) recipeElement.getElementsByTagName("properties").item(0);
		return handleRecipe(input, output, properties);
	}
	
	protected abstract ItemStack handleRecipe(Element input, Element output, Element properties)
			throws XmlRecipeException;

}
