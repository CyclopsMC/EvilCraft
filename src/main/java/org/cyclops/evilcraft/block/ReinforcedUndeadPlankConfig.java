package org.cyclops.evilcraft.block;

import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link ReinforcedUndeadPlank}.
 * @author rubensworks
 *
 */
public class ReinforcedUndeadPlankConfig extends BlockConfig {

    /**
     * The unique instance.
     */
    public static ReinforcedUndeadPlankConfig _instance;

    /**
     * Make a new instance.
     */
    public ReinforcedUndeadPlankConfig() {
        super(
                EvilCraft._instance,
        	true,
            "reinforcedUndeadPlank",
            null,
            ReinforcedUndeadPlank.class
        );
    }
    
    @Override
    public boolean isMultipartEnabled() {
        return true;
    }
    
}
