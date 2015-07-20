package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

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
                EvilCraft._instance,
        	true,
            "bloodPearlOfTeleportation",
            null,
            BloodPearlOfTeleportation.class
        );
    }
    
    @Override
	protected String getConfigPropertyPrefix() {
		return "bloodPearl";
	}
    
}
