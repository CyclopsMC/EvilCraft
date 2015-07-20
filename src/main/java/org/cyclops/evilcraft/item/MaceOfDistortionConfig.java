package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link MaceOfDistortion}.
 * @author rubensworks
 *
 */
public class MaceOfDistortionConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static MaceOfDistortionConfig _instance;

    /**
     * Make a new instance.
     */
    public MaceOfDistortionConfig() {
        super(
                EvilCraft._instance,
            true,
            "maceOfDistortion",
            null,
            MaceOfDistortion.class
        );
    }
    
}
