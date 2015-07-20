package org.cyclops.evilcraft.potion;

import org.cyclops.cyclopscore.config.extendedconfig.PotionConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

/**
 * Config for {@link PotionPaling}.
 * @author rubensworks
 *
 */
public class PotionPalingConfig extends PotionConfig {

    /**
     * The unique instance.
     */
    public static PotionPalingConfig _instance;

    /**
     * Make a new instance.
     */
    public PotionPalingConfig() {
        super(
            EvilCraft._instance,
            Reference.POTION_PALING,
            "paling",
            null,
            PotionPaling.class
        );
    }
    
}
