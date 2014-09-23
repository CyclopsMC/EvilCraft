package evilcraft.modcompat.bloodmagic;

import evilcraft.core.config.ConfigurableProperty;
import evilcraft.core.config.ConfigurableTypeCategory;
import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * Config for the {@link BoundBloodDrop}.
 * @author rubensworks
 *
 */
public class BoundBloodDropConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static BoundBloodDropConfig _instance;
    
    /**
     * The maximum capacity in (Blood) mB that can be filled.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "The maximum capacity in (Blood) mB that can be filled.", isCommandable = true)
    public static int maxCapacity = 250000;

    /**
     * Make a new instance.
     */
    public BoundBloodDropConfig() {
        super(
        	true,
            "boundBloodDrop",
            null,
            BoundBloodDrop.class
        );
    }
    
}
