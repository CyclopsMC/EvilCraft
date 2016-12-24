package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link CreativeBloodDrop}.
 * @author rubensworks
 *
 */
public class CreativeBloodDropConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static CreativeBloodDropConfig _instance;

    /**
     * Make a new instance.
     */
    public CreativeBloodDropConfig() {
        super(
                EvilCraft._instance,
        	true,
            "creative_blood_drop",
            null,
            CreativeBloodDrop.class
        );
    }
    
}
