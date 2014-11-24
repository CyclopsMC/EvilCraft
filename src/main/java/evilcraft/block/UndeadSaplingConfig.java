package evilcraft.block;

import evilcraft.Reference;
import evilcraft.core.config.extendedconfig.BlockConfig;

/**
 * Config for the Undead Sapling.
 * @author rubensworks
 *
 */
public class UndeadSaplingConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static UndeadSaplingConfig _instance;

    /**
     * Make a new instance.
     */
    public UndeadSaplingConfig() {
        super(
        	true,
            "undeadSapling",
            null,
            UndeadSapling.class
        );
    }
    
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_SAPLINGTREE;
    }
    
    @Override
    public boolean isDisableable() {
        return false;
    }
    
}
