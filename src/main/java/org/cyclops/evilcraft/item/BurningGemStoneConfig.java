package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link BurningGemStone}.
 * @author rubensworks
 *
 */
public class BurningGemStoneConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static BurningGemStoneConfig _instance;
    
    /**
     * How much damage this item can take.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "How much damage this item can take.")
    public static int maxDamage = 64;

    /**
     * Make a new instance.
     */
    public BurningGemStoneConfig() {
        super(
                EvilCraft._instance,
        	true,
            "burningGemStone",
            null,
            BurningGemStone.class
        );
    }
    
}
