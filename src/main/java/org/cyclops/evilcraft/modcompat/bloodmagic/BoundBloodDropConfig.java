package org.cyclops.evilcraft.modcompat.bloodmagic;

import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

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
     * Max update frequency
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.WORLDGENERATION, comment = "The amount of ticks the server should wait before sending a soul network update. (only for servers)")
    public static int maxUpdateTicks = 40;

    /**
     * Make a new instance.
     */
    public BoundBloodDropConfig() {
        super(
                EvilCraft._instance,
                true,
                "boundBloodDrop",
                null,
                BoundBloodDrop.class
        );
    }
    
}
