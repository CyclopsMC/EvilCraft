package evilcraft.item;

import evilcraft.core.config.ConfigurableProperty;
import evilcraft.core.config.ConfigurableTypeCategory;
import evilcraft.core.config.extendedconfig.ItemConfig;

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
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "If the Kineticator should also attract XP orbs.", isCommandable = true)
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
