package evilcraft.core.degradation.effects;

import evilcraft.biomes.BiomeDegraded;
import evilcraft.core.config.DegradationEffectConfig;

/**
 * Config for {@link BiomeDegraded}.
 * @author rubensworks
 *
 */
public class SoundDegradationConfig extends DegradationEffectConfig {
    
    /**
     * The unique instance.
     */
    public static SoundDegradationConfig _instance;

    /**
     * Make a new instance.
     */
    public SoundDegradationConfig() {
        super(
            true,
            "sound",
            null,
            SoundDegradation.class,
            3
        );
    }
    
}
