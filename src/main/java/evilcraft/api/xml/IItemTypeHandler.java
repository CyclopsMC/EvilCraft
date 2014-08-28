package evilcraft.api.xml;

import org.w3c.dom.Node;

import evilcraft.api.xml.XmlRecipeLoader.XmlRecipeException;

/**
 * Handler for different types of item declaration inside the xml recipe file.
 * @author rubensworks
 *
 */
public interface IItemTypeHandler {

	/**
	 * Get the item for this type of handler for the given node element, can just be a text node.
	 * @param itemNode The node containing info about this item, for example
	 * "<item>evilcraft:darkGem</item>"
	 * @return An item stack for this item or a string representing an ore dict id.
	 * @throws XmlRecipeException If an error occured
	 */
	public Object getItem(Node itemNode) throws XmlRecipeException;
	
}
