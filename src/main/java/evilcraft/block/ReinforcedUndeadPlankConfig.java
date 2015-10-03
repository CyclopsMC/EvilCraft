package evilcraft.block;

import evilcraft.core.config.extendedconfig.BlockConfig;

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
