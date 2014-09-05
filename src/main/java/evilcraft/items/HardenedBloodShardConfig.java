package evilcraft.items;

import evilcraft.Reference;
import evilcraft.core.config.ItemConfig;

/**
 * Config for the {@link HardenedBloodShard}.
 * @author rubensworks
 *
 */
public class HardenedBloodShardConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static HardenedBloodShardConfig _instance;

    /**
     * Make a new instance.
     */
    public HardenedBloodShardConfig() {
        super(
        	true,
            "hardenedBloodShard",
            null,
            HardenedBloodShard.class
        );
    }
    
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_DYERED;
    }
    
}
