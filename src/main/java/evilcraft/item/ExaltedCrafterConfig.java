package evilcraft.item;

import evilcraft.core.config.ConfigurableProperty;
import evilcraft.core.config.ConfigurableTypeCategory;
import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * Config for the {@link ExaltedCrafter}.
 * @author rubensworks
 *
 */
public class ExaltedCrafterConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static ExaltedCrafterConfig _instance;
    
    /**
     * If shift clicking on an item should first try to go into the crafting grid.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "If shift clicking on an item should first try to go into the crafting grid.", isCommandable = true)
    public static boolean shiftClickToCraftingGrid = false;

    /**
     * Make a new instance.
     */
    public ExaltedCrafterConfig() {
        super(
        	true,
            "exaltedCrafter",
            null,
            ExaltedCrafter.class
        );
    }
    
}
