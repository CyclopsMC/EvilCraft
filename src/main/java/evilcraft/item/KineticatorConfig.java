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
     * The amount of ticks in between each area checking for items.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "The amount of ticks inbetween each area checking for items.", isCommandable = true)
    public static int tickHoldoff = 1;

    /**
     * The amount of ticks in between each blood consumption when there are valid items in the area.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "The amount of ticks in between each blood consumption when there are valid items in the area.", isCommandable = true)
    public static int consumeHoldoff = 20;

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
