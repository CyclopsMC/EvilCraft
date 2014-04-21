package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ElementTypeCategory;
import evilcraft.api.config.ItemConfig;
import evilcraft.api.config.configurable.ConfigurableProperty;

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
            Reference.ITEM_KINETICATOR,
            "Kineticator",
            "kineticator",
            null,
            Kineticator.class
        );
    }
    
}
