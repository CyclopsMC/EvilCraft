package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

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
                EvilCraft._instance,
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
