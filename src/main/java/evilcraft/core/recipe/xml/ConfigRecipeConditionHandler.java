package evilcraft.core.recipe.xml;

import evilcraft.core.config.ConfigHandler;
import evilcraft.core.config.ExtendedConfig;

/**
 * Condition handler for checking if configs are enabled.
 * @author rubensworks
 *
 */
public class ConfigRecipeConditionHandler implements IRecipeConditionHandler {

	@Override
	public boolean isSatisfied(String param) {
		ExtendedConfig<?> config = ConfigHandler.getInstance().getDictionary().get(param);
		return config != null && config.isEnabled();
	}

}
