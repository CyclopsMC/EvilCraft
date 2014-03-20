package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ElementTypeCategory;
import evilcraft.api.config.ItemConfig;
import evilcraft.api.config.configurable.ConfigurableProperty;

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
    @ConfigurableProperty(category = ElementTypeCategory.GENERAL, comment = "If crafting of the ender pearl should be enabled.")
    public static boolean enderPearlRecipe = true;

    /**
     * Make a new instance.
     */
    public PotentiaSphereConfig() {
        super(
            Reference.ITEM_POTENTIASPHERE,
            "Potentia Sphere",
            "potentiaSphere",
            null,
            PotentiaSphere.class
        );
    }
    
}
