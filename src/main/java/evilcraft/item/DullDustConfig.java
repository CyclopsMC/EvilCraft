package evilcraft.item;

import evilcraft.Reference;
import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * Config for the Dull Dust.
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
            null
        );
    }
    
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_DUSTDULL;
    }
    
}
