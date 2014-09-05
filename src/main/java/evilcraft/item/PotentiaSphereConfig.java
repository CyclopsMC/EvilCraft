package evilcraft.item;

import evilcraft.core.config.ElementTypeCategory;
import evilcraft.core.config.ItemConfig;
import evilcraft.core.config.configurable.ConfigurableProperty;

/**
 * Config for the {@link PotentiaSphere}.
 * @author rubensworks
 *
 */
public class PotentiaSphereConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static PotentiaSphereConfig _instance;
    
    /**
     * If crafting of the ender pearl should be enabled.
     */
    @ConfigurableProperty(category = ElementTypeCategory.GENERAL, comment = "If crafting of the ender pearl should be enabled.", requiresMcRestart = true)
    public static boolean enderPearlRecipe = true;

    /**
     * Make a new instance.
     */
    public PotentiaSphereConfig() {
        super(
            true,
            "potentiaSphere",
            null,
            PotentiaSphere.class
        );
    }
    
}
