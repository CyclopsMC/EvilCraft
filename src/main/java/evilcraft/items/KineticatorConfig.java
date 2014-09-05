package evilcraft.items;

import evilcraft.core.config.ElementTypeCategory;
import evilcraft.core.config.ItemConfig;
import evilcraft.core.config.configurable.ConfigurableProperty;

/**
 * Config for the {@link Kineticator}.
 * @author rubensworks
 *
 */
public class KineticatorConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static KineticatorConfig _instance;
    
    /**
     * If the Kineticator should also attract XP orbs.
     */
    @ConfigurableProperty(category = ElementTypeCategory.GENERAL, comment = "If the Kineticator should also attract XP orbs.")
    public static boolean moveXP = true;

    /**
     * Make a new instance.
     */
    public KineticatorConfig() {
        super(
        	true,
            "kineticator",
            null,
            Kineticator.class
        );
    }
    
}
