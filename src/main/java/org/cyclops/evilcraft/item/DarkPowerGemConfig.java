package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

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
                EvilCraft._instance,
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
