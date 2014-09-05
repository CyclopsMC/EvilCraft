package evilcraft.items;

import evilcraft.core.config.ElementTypeCategory;
import evilcraft.core.config.ItemConfig;
import evilcraft.core.config.configurable.ConfigurableProperty;

/**
 * Config for the {@link VengeanceRing}.
 * @author rubensworks
 *
 */
public class VengeanceRingConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static VengeanceRingConfig _instance;
    
    /**
     * The area of effect in # blocks of this ring.
     */
    @ConfigurableProperty(category = ElementTypeCategory.GENERAL, comment = "The area of effect in # blocks of this ring.")
    public static int areaOfEffect = 10;

    /**
     * Make a new instance.
     */
    public VengeanceRingConfig() {
        super(
        	true,
            "vengeanceRing",
            null,
            VengeanceRing.class
        );
    }
    
}
