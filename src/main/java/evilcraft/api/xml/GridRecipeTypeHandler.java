package evilcraft.api.xml;

import net.minecraft.item.ItemStack;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import evilcraft.api.xml.XmlRecipeLoader.XmlRecipeException;

/**
 * Common handler for both shaped and shapeless recipes.
 * @author rubensworks
 *
 */
public abstract class GridRecipeTypeHandler extends CommonRecipeTypeHandler {
	
	@Override
	public void loadRecipe(Node recipe) {
		Element recipeElement = (Element) recipe;
		Element input = (Element) recipeElement.getElementsByTagName("input").item(0);
		Element output = (Element) recipeElement.getElementsByTagName("output").item(0);
		
		ItemStack outputItem = (ItemStack) getItem(output.getElementsByTagName("item").item(0));
		
		handleIO(input, outputItem);
	}
	
	protected abstract void handleIO(Element input, ItemStack output) throws XmlRecipeException;
	
}
