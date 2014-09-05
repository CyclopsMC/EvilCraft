package evilcraft.blocks;

import evilcraft.core.config.BlockConfig;

/**
 * A config for {@link InvisibleRedstoneBlock}.
 * @author rubensworks
 *
 */
public class InvisibleRedstoneBlockConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static InvisibleRedstoneBlockConfig _instance;

    /**
     * Make a new instance.
     */
    public InvisibleRedstoneBlockConfig() {
        super(
        		true,
        		"invisibleRedstoneBlock",
        		null,
        		InvisibleRedstoneBlock.class
        );
    }
    
}
