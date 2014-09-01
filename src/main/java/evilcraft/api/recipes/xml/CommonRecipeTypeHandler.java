package evilcraft.api.recipes.xml;

import java.util.Map;

import org.w3c.dom.Node;

import com.google.common.collect.Maps;

import evilcraft.api.recipes.xml.XmlRecipeLoader.XmlRecipeException;

/**
 * A default implementation of {@link IRecipeTypeHandler} with some helper methods and
 * {@link IItemTypeHandler}'s.
 * @author rubensworks
 *
 */
public abstract class CommonRecipeTypeHandler implements IRecipeTypeHandler, IItemTypeHandler {

	private static final Map<String, IItemTypeHandler> ITEM_TYPE_HANDLERS = Maps.newHashMap();
	protected static final String DEFAULT_ITEM_TYPE = "default";
	static {
		ITEM_TYPE_HANDLERS.put(DEFAULT_ITEM_TYPE, new DefaultItemTypeHandler());
		ITEM_TYPE_HANDLERS.put("oredict", new OreDictItemTypeHandler());
		ITEM_TYPE_HANDLERS.put("predefined", new PredefinedItemTypeHandler());
	}
	
	/**
	 * Register a new item type handler.
	 * @param type The type name.
	 * @param handler The handler instance.
	 */
	public static void registerItemTypeHandler(String type, IItemTypeHandler handler) {
		ITEM_TYPE_HANDLERS.put(type, handler);
	}
	
	@Override
	public Object getItem(Node itemNode) throws XmlRecipeException {
		String type = DEFAULT_ITEM_TYPE;
		Node typeAttribute = itemNode.getAttributes().getNamedItem("type");
		if(typeAttribute != null) {
			type = typeAttribute.getTextContent();
		}
		return getItem(itemNode, type);
	}
	
	protected Object getItem(Node itemNode, String type) throws XmlRecipeException {
		IItemTypeHandler handler = ITEM_TYPE_HANDLERS.get(type);
		if(handler == null) {
			throw new XmlRecipeException(String.format(
					"Could not find an item type handler of type '%s'", type));
		}
		return handler.getItem(itemNode);
	}
	
}
