package evilcraft.core.recipe.xml;

import evilcraft.EvilCraft;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

/**
 * Condition handler for checking if configs are enabled.
 * @author rubensworks
 *
 */
public class ConfigRecipeConditionHandler implements IRecipeConditionHandler {

	@Override
	public boolean isSatisfied(String param) {
		ExtendedConfig<?> config = EvilCraft._instance.getConfigHandler().getDictionary().get(param);
		return config != null && config.isEnabled();
	}

}
