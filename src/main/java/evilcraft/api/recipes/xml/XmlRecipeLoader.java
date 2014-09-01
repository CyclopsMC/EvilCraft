package evilcraft.api.recipes.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import net.minecraft.item.ItemStack;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * An XML Recipe loader.
 * @author rubensworks
 */
public class XmlRecipeLoader {

	private static final Map<String, IRecipeTypeHandler> RECIPE_TYPE_HANDLERS = Maps.newHashMap();
	static {
		RECIPE_TYPE_HANDLERS.put("shaped", new ShapedRecipeTypeHandler());
		RECIPE_TYPE_HANDLERS.put("shapeless", new ShapelessRecipeTypeHandler());
		RECIPE_TYPE_HANDLERS.put("smelting", new SmeltingRecipeTypeHandler());
		RECIPE_TYPE_HANDLERS.put("evilcraft:bloodinfuser", new BloodInfuserRecipeTypeHandler());
	}
	
	private static final Map<String, IRecipeConditionHandler> RECIPE_CONDITION_HANDLERS =
			Maps.newHashMap();
	static {
		RECIPE_CONDITION_HANDLERS.put("config", new ConfigRecipeConditionHandler());
		RECIPE_CONDITION_HANDLERS.put("predefined", new PredefinedRecipeConditionHandler());
	}
	
	private static final Map<String, ItemStack> PREDEFINED_ITEMS = Maps.newHashMap();
	private static final Set<String> PREDEFINED_VALUES = Sets.newHashSet();
	
	private StreamSource stream;
	private InputStream xsdIs = null;
	private Document doc = null;
	
	/**
	 * Register a new recipe type handler.
	 * @param type The type name.
	 * @param handler The handler instance.
	 */
	public static void registerRecipeTypeHandler(String type, IRecipeTypeHandler handler) {
		RECIPE_TYPE_HANDLERS.put(type, handler);
	}
	
	/**
	 * Register a new recipe condition handler.
	 * @param type The type name.
	 * @param handler The handler instance.
	 */
	public static void registerRecipeConditionHandler(String type, IRecipeConditionHandler handler) {
		RECIPE_CONDITION_HANDLERS.put(type, handler);
	}
	
	/**
	 * Register a new predefined item.
	 * @param key The key of the item.
	 * @param item The item.
	 */
	public static void registerPredefinedItem(String key, ItemStack item) {
		PREDEFINED_ITEMS.put(key, item);
	}
	
	/**
	 * Get a predefined item by key.
	 * @param key The key of the item.
	 * @return The item or null.
	 */
	public static ItemStack getPredefinedItem(String key) {
		return PREDEFINED_ITEMS.get(key);
	}
	
	/**
	 * Register a new predefined value that can be used in {@link PredefinedRecipeConditionHandler}.
	 * @param value The key to register.
	 */
	public static void registerPredefinedValue(String value) {
		PREDEFINED_VALUES.add(value);
	}
	
	/**
	 * Check if a value has been predefined.
	 * @param value The key to check.
	 * @return If it was predefined.
	 */
	public static boolean isPredefinedValue(String value) {
		return PREDEFINED_VALUES.contains(value);
	}
	
	/**
	 * Make a new loader for the given file.
	 * @param is The stream containing xml recipes.
	 */
	public XmlRecipeLoader(InputStream is) {
		this.stream = new StreamSource(is);
	}
	
	/**
	 * Set the XSD validator.
	 * @param xsdIs The inputstream for the validator.
	 */
	public void setValidator(InputStream xsdIs) {
		this.xsdIs = xsdIs;
	}
	
	/**
	 * Validate the xml file.
	 * @throws XmlRecipeException If the file was invalid.
	 */
	public void validate() throws XmlRecipeException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			if(xsdIs != null) {
				SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		        Schema schema = factory.newSchema(new StreamSource(xsdIs));
				dbFactory.setSchema(schema);
			}
			
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(stream.getInputStream());
		} catch (SAXException e) {
			throw new XmlRecipeException(e);
		} catch (IOException e) {
			throw new XmlRecipeException(e);
		} catch (ParserConfigurationException e) {
			throw new XmlRecipeException(e);
		}
	}
	
	/**
	 * Load the recipes for this instance.
	 * @throws XmlRecipeException If something went wrong.
	 */
	public void loadRecipes() throws XmlRecipeException {
		if(doc == null) {
			validate();
		}
		
		NodeList recipes = doc.getElementsByTagName("recipe");
		for(int i = 0; i < recipes.getLength(); i++) {
			Element recipe = (Element) recipes.item(i);
			if(isRecipeEnabled(recipe)) {
				handleRecipe(recipe);
			}
		}
	}
	
	private boolean isRecipeEnabled(Element recipe) {
		boolean enable = true;
		NodeList conditions = recipe.getElementsByTagName("condition");
		int j = 0;
		while(j < conditions.getLength() && enable) {
			Node condition = conditions.item(j);
			String conditionType = condition.getAttributes().getNamedItem("type").getTextContent();
			IRecipeConditionHandler handler = RECIPE_CONDITION_HANDLERS.get(conditionType);
			if(handler == null) {
				throw new XmlRecipeException(String.format(
						"Could not find a recipe condition handler of type '%s'", conditionType));
			}
			String param = condition.getTextContent();
			enable = enable && handler.isSatisfied(param);
			j++;
		}
		return enable;
	}
	
	private void handleRecipe(Element recipe) throws XmlRecipeException {
		String type = recipe.getAttributes().getNamedItem("type").getTextContent();
		IRecipeTypeHandler handler = RECIPE_TYPE_HANDLERS.get(type);
		if(handler == null) {
			throw new XmlRecipeException(String.format(
					"Could not find a recipe type handler of type '%s'", type));
		}
		handler.loadRecipe(recipe);
	}
	
	/**
	 * Error that can occur while reading xml recipes.
	 * @author rubensworks
	 */
	public static class XmlRecipeException extends RuntimeException {
		
		/**
		 * Make a new instance.
		 * @param message The message.
		 */
		public XmlRecipeException(String message) {
			super(message);
		}
		
		/**
		 * Make a new instance.
		 * @param e The exception with a message.
		 */
		public XmlRecipeException(Exception e) {
			super(e.getMessage());
		}
		
	}
	
}
