package evilcraft.item;

import evilcraft.Reference;
import evilcraft.core.config.extendedconfig.ItemConfig;

/**
 * Config for the Poison Sac.
 * @author rubensworks
 *
 */
public class PoisonSacConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static PoisonSacConfig _instance;

    /**
     * Make a new instance.
     */
    public PoisonSacConfig() {
        super(
        	true,
            "poisonSac",
            null,
            null
        );
    }
    
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_MATERIALPOISONOUS;
    }
    
}
