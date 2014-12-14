package evilcraft.item;

import evilcraft.core.config.configurable.ConfigurableItem;
import evilcraft.core.config.configurable.IConfigurable;
import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * Config for the Dark Power Gem.
 * @author rubensworks
 *
 */
public class DarkPowerGemConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static DarkPowerGemConfig _instance;

    /**
     * Make a new instance.
     */
    public DarkPowerGemConfig() {
        super(
        	true,
            "darkPowerGem",
            null,
            null
        );
    }

    @Override
    protected IConfigurable initSubInstance() {
        return(ConfigurableItem) new ConfigurableItem(this).setMaxStackSize(16);
    }
    
}
