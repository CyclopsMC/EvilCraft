package evilcraft.blocks;

import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;

/**
 * Config for the {@link EvilBlock}.
 * @author rubensworks
 *
 */
public class EvilBlockConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static EvilBlockConfig _instance;

    /**
     * Make a new instance.
     */
    public EvilBlockConfig() {
        super(
            Reference.BLOCK_EVILBLOCK,
            "EvilBlock",
            "evilBlock",
            null,
            EvilBlock.class
        );
    }
    
}
