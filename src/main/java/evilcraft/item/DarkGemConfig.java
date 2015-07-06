package evilcraft.item;

import evilcraft.EvilCraft;
import evilcraft.Reference;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;

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
            EvilCraft._instance,
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
