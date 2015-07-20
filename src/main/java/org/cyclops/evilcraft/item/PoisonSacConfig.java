package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

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
                EvilCraft._instance,
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
