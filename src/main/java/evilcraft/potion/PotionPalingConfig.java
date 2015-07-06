package evilcraft.potion;

import evilcraft.EvilCraft;
import evilcraft.Reference;
import org.cyclops.cyclopscore.config.extendedconfig.PotionConfig;

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
            EvilCraft._instance,
            Reference.POTION_PALING,
            "paling",
            null,
            PotionPaling.class
        );
    }
    
}
