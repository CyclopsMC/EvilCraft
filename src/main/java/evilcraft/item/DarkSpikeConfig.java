package evilcraft.item;

import evilcraft.Reference;
import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * Config for the {@link DarkSpike}.
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
        	true,
            "darkSpike",
            null,
            DarkSpike.class
        );
    }
    
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_MATERIALSPIKE;
    }
    
}
