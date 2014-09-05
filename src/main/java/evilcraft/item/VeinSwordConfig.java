package evilcraft.item;

import evilcraft.core.config.ElementTypeCategory;
import evilcraft.core.config.ItemConfig;
import evilcraft.core.config.configurable.ConfigurableProperty;

/**
 * Config for the {@link VeinSword}.
 * @author rubensworks
 *
 */
public class VeinSwordConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static VeinSwordConfig _instance;
    
    /**
     * The multiply boost this sword has on the blood that is obtained.
     */
    @ConfigurableProperty(category = ElementTypeCategory.GENERAL, comment = "The multiply boost this sword has on the blood that is obtained.", isCommandable = true)
    public static double extractionBoost = 2.0;

    /**
     * Make a new instance.
     */
    public VeinSwordConfig() {
        super(
        	true,
            "veinSword",
            null,
            VeinSword.class
        );
    }
    
}
