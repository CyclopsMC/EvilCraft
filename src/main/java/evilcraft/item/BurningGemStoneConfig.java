package evilcraft.item;

import evilcraft.core.config.ConfigurableProperty;
import evilcraft.core.config.ConfigurableTypeCategory;
import evilcraft.core.config.extendedconfig.ItemConfig;

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
