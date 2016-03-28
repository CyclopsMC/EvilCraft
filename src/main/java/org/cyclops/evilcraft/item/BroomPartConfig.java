package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link BroomPart}.
 * @author rubensworks
 *
 */
public class BroomPartConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static BroomPartConfig _instance;

    /**
     * Make a new instance.
     */
    public BroomPartConfig() {
        super(
            EvilCraft._instance,
        	true,
            "broomPart",
            null,
            BroomPart.class
        );
    }
    
}
