package evilcraft.api.recipes.xml;


/**
 * Condition handler for checking if configs are enabled.
 * @author rubensworks
 *
 */
public class PredefinedRecipeConditionHandler implements IRecipeConditionHandler {

	@Override
	public boolean isSatisfied(String param) {
		return XmlRecipeLoader.isPredefinedValue(param);
	}

}
