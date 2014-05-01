package evilcraft.api.degradation.effects;

import evilcraft.api.config.DegradationEffectConfig;
import evilcraft.biomes.BiomeDegraded;

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
