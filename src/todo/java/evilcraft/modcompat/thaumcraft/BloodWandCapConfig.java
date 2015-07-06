package evilcraft.modcompat.thaumcraft;

import evilcraft.core.config.configurable.ConfigurableItem;
import evilcraft.core.config.configurable.IConfigurable;
import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * Blood Infused Golden Wand cap that has a slightly higher discount.
 * Textures are based on the ones from Thaumcraft.
 * @author rubensworks
 *
 */
public class BloodWandCapConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static BloodWandCapConfig _instance;

    /**
     * Make a new instance.
     */
    public BloodWandCapConfig() {
        super(
        	true,
            "bloodWandCap",
            null,
            null
        );
    }

    @Override
    protected IConfigurable initSubInstance() {
        return new ConfigurableItem(this);
    }
    
}
