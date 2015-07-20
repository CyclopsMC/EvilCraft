package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

/**
 * Config for the Hardened Blood Shard.
 * @author rubensworks
 *
 */
public class HardenedBloodShardConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static HardenedBloodShardConfig _instance;
    
    /**
     * The minimum amount of shards from when using flint 'n steel on Hardened Blood.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "The minimum amount of shards from when using flint 'n steel on Hardened Blood.", isCommandable = true)
    public static int minimumDropped = 5;
    
    /**
     * The additional random amount of shards from when using flint 'n steel on Hardened Blood.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "The additional random amount of shards from when using flint 'n steel on Hardened Blood.", isCommandable = true)
    public static int additionalDropped = 4;

    /**
     * Make a new instance.
     */
    public HardenedBloodShardConfig() {
        super(
                EvilCraft._instance,
        	true,
            "hardenedBloodShard",
            null,
            null
        );
    }
    
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_DYERED;
    }
    
}
