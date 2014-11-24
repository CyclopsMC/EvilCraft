package evilcraft.item;

import evilcraft.core.config.ConfigurableProperty;
import evilcraft.core.config.ConfigurableTypeCategory;
import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * Config for the Potentia Sphere.
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
    @ConfigurableProperty(category = ConfigurableTypeCategory.GENERAL, comment = "If crafting of the ender pearl should be enabled.", requiresMcRestart = true)
    public static boolean enderPearlRecipe = true;

    /**
     * Make a new instance.
     */
    public PotentiaSphereConfig() {
        super(
            true,
            "potentiaSphere",
            null,
            null
        );
    }
    
}
