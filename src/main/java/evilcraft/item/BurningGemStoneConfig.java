package evilcraft.item;

import evilcraft.core.config.ElementTypeCategory;
import evilcraft.core.config.ItemConfig;
import evilcraft.core.config.configurable.ConfigurableProperty;

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
    @ConfigurableProperty(category = ElementTypeCategory.ITEM, comment = "How much damage this item can take.")
    public static int maxDamage = 128;

    /**
     * Make a new instance.
     */
    public BurningGemStoneConfig() {
        super(
        	true,
            "burningGemStone",
            null,
            BurningGemStone.class
        );
    }
    
}
