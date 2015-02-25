package evilcraft.core.recipe.xml;

import evilcraft.core.recipe.xml.XmlRecipeLoader.XmlRecipeException;
import org.w3c.dom.Node;

/**
 * Handler for different types of item declaration inside the xml recipe file.
 * @author rubensworks
 *
 */
public interface IItemTypeHandler {

	/**
	 * Get the item for this type of handler for the given node element, can just be a text node.
	 * @param itemNode The node containing info about this item, for example
	 * "&lt;item&gt;evilcraft:darkGem&lt;/item&gt;"
	 * @return An item stack for this item or a string representing an ore dict id.
	 * @throws XmlRecipeException If an error occured
	 */
	public Object getItem(Node itemNode) throws XmlRecipeException;
	
}
