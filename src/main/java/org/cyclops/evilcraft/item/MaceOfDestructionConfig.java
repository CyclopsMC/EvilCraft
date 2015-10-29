package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link MaceOfDestruction}.
 * @author rubensworks
 *
 */
public class MaceOfDestructionConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static MaceOfDestructionConfig _instance;

    /**
     * Make a new instance.
     */
    public MaceOfDestructionConfig() {
        super(
                EvilCraft._instance,
            true,
            "maceOfDestruction",
            null,
            MaceOfDestruction.class
        );
    }
    
}
