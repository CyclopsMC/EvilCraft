package evilcraft.blocks;

import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;

/**
 * Config for the {@link UndeadLeaves}.
 * @author rubensworks
 *
 */
public class UndeadLeavesConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static UndeadLeavesConfig _instance;

    /**
     * Make a new instance.
     */
    public UndeadLeavesConfig() {
        super(
            Reference.BLOCK_UNDEADLEAVES,
            "Undead Leaves",
            "undeadLeaves",
            null,
            UndeadLeaves.class
        );
    }
    
    @Override
    public boolean isDisableable() {
        return false;
    }
    
}
