package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

/**
 * Config for the Dark Spike.
 * @author rubensworks
 *
 */
public class DarkSpikeConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static DarkSpikeConfig _instance;

    /**
     * Make a new instance.
     */
    public DarkSpikeConfig() {
        super(
                EvilCraft._instance,
        	true,
            "darkSpike",
            null,
            null
        );
    }
    
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_MATERIALSPIKE;
    }
    
}
