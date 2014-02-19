package evilcraft.blocks;

import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;

/**
 * Config for the {@link UndeadPlank}.
 * @author rubensworks
 *
 */
public class UndeadPlankConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static UndeadPlankConfig _instance;

    /**
     * Make a new instance.
     */
    public UndeadPlankConfig() {
        super(
            "undeadPlank",
            null,
            UndeadPlank.class
        );
    }
    
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_WOODPLANK;
    }
    
    @Override
    public boolean isMultipartEnabled() {
        return true;
    }
    
}
