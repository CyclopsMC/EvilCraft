package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ItemConfig;

/**
 * Config for the {@link PoisonSac}.
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
            Reference.ITEM_POISONSAC,
            "Poison Sac",
            "poisonSac",
            null,
            PoisonSac.class
        );
    }
    
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_MATERIALPOISONOUS;
    }
    
}
