package evilcraft.item;

import evilcraft.core.config.ConfigurableProperty;
import evilcraft.core.config.ConfigurableTypeCategory;
import evilcraft.core.config.extendedconfig.ItemConfig;

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
        	true,
            "rejuvenatedFlesh",
            null,
            RejuvenatedFlesh.class
        );
    }
    
}
