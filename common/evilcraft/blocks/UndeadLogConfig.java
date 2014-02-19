package evilcraft.blocks;

import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;

/**
 * Config for the {@link UndeadLog}.
 * @author rubensworks
 *
 */
public class UndeadLogConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static UndeadLogConfig _instance;

    /**
     * Make a new instance.
     */
    public UndeadLogConfig() {
        super(
            "undeadLog",
            null,
            UndeadLog.class
        );
    }
    
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_WOODLOG;
    }
    
    @Override
    public boolean isMultipartEnabled() {
        return true;
    }
    
}
