package org.cyclops.evilcraft.core.degradation.effect;

import org.cyclops.evilcraft.core.config.extendedconfig.DegradationEffectConfig;
import org.cyclops.evilcraft.world.biome.BiomeDegraded;

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
