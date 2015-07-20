package org.cyclops.evilcraft.block;

import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

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
                EvilCraft._instance,
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
