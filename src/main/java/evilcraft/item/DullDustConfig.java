package evilcraft.item;

import evilcraft.Reference;
import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * Config for the {@link evilcraft.item.DullDust}.
 * @author rubensworks
 *
 */
public class DullDustConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static DullDustConfig _instance;

    /**
     * Make a new instance.
     */
    public DullDustConfig() {
        super(
        	true,
            "dullDust",
            null,
            DullDust.class
        );
    }
    
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_DUSTDULL;
    }
    
}
