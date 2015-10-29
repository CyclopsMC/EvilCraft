package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Rejuvenated Flesh.
 * @author rubensworks
 *
 */
public class RejuvenatedFleshConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static RejuvenatedFleshConfig _instance;
    /**
     * The amount of blood (mB) this container can hold.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "The amount of blood (mB) this container can hold.", requiresMcRestart = true)
    public static int containerSize = 10000;
    /**
     * The amount of blood (mB) that is consumed per bite.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "The amount of blood (mB) that is consumed per bite.")
    public static int biteUsage = 250;

    /**
     * Make a new instance.
     */
    public RejuvenatedFleshConfig() {
        super(
                EvilCraft._instance,
        	true,
            "rejuvenatedFlesh",
            null,
            RejuvenatedFlesh.class
        );
    }
    
}
