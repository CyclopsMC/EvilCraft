package evilcraft.potion;

import evilcraft.Reference;
import evilcraft.core.config.extendedconfig.PotionConfig;

/**
 * Config for {@link evilcraft.potion.PotionPaling}.
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
            Reference.POTION_PALING,
            "paling",
            null,
            PotionPaling.class
        );
    }
    
}
