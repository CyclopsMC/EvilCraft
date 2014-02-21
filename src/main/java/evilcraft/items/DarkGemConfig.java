package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ItemConfig;

/**
 * Config for the {@link DarkGem}.
 * @author rubensworks
 *
 */
public class DarkGemConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static DarkGemConfig _instance;

    /**
     * Make a new instance.
     */
    public DarkGemConfig() {
        super(
        	true,
            "darkGem",
            null,
            DarkGem.class
        );
    }
    
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_GEMDARK;
    }
    
}
