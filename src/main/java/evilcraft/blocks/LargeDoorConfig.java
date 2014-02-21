package evilcraft.blocks;

import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;

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
        	false,
            "largeDoor",
            null,
            LargeDoor.class
        );
    }
    
}
