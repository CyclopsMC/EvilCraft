package evilcraft.item;

import evilcraft.core.config.ConfigurableProperty;
import evilcraft.core.config.ConfigurableTypeCategory;
import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * Config for the {@link BloodPearlOfTeleportation}.
 * @author rubensworks
 *
 */
public class BloodPearlOfTeleportationConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static BloodPearlOfTeleportationConfig _instance;
    
    /**
     * The amount of second slowness should be applied after each teleport.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "The amount of second slowness should be applied after each teleport.", isCommandable = true)
    public static int slownessDuration = 0;

    /**
     * Make a new instance.
     */
    public BloodPearlOfTeleportationConfig() {
        super(
        	true,
            "bloodPearlOfTeleportation",
            null,
            BloodPearlOfTeleportation.class
        );
    }
    
    @Override
    public boolean blendAlpha() {
        return true;
    }
    
}
