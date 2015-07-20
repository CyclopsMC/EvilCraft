package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

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
                EvilCraft._instance,
        	true,
            "vengeanceRing",
            null,
            VengeanceRing.class
        );
    }
    
}
