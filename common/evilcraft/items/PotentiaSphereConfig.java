package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ItemConfig;

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
