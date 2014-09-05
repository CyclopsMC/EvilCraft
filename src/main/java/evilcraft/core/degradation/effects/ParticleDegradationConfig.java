package evilcraft.core.degradation.effects;

import evilcraft.core.config.DegradationEffectConfig;

/**
 * Config for {@link ParticleDegradation}.
 * @author rubensworks
 *
 */
public class ParticleDegradationConfig extends DegradationEffectConfig {
    
    /**
     * The unique instance.
     */
    public static ParticleDegradationConfig _instance;

    /**
     * Make a new instance.
     */
    public ParticleDegradationConfig() {
        super(
            true,
            "particle",
            null,
            ParticleDegradation.class,
            10
        );
    }
    
}
