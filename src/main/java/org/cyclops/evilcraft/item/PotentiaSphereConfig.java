package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

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
                EvilCraft._instance,
            true,
            "potentiaSphere",
            null,
            null
        );
    }
    
}
