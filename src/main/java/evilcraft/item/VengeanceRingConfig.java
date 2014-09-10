package evilcraft.item;

import evilcraft.core.config.ConfigurableProperty;
import evilcraft.core.config.ConfigurableTypeCategory;
import evilcraft.core.config.extendedconfig.ItemConfig;

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
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "The area of effect in # blocks of this ring.", isCommandable = true)
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
