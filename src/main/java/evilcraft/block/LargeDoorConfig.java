package evilcraft.block;

import evilcraft.core.config.BlockConfig;

/**
 * Config for the {@link LargeDoor}.
 * @author rubensworks
 *
 */
public class LargeDoorConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static LargeDoorConfig _instance;

    /**
     * Make a new instance.
     */
    public LargeDoorConfig() {
        super(
        	true,
            "largeDoor",
            null,
            LargeDoor.class
        );
    }
    
    @Override
    public boolean isHardDisabled() {
    	return true;
    }
    
}
