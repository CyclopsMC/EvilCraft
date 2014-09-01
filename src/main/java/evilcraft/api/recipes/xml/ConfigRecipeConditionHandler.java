package evilcraft.api.recipes.xml;

import evilcraft.api.config.ConfigHandler;
import evilcraft.api.config.ExtendedConfig;

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
