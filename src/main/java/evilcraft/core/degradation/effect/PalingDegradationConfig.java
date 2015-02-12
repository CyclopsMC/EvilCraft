package evilcraft.core.degradation.effect;

import evilcraft.core.config.extendedconfig.DegradationEffectConfig;

/**
 * Config for {@link evilcraft.core.degradation.effect.PalingDegradation}.
 * @author rubensworks
 *
 */
public class PalingDegradationConfig extends DegradationEffectConfig {

    /**
     * The unique instance.
     */
    public static PalingDegradationConfig _instance;

    /**
     * Make a new instance.
     */
    public PalingDegradationConfig() {
        super(
            true,
            "paling",
            null,
            PalingDegradation.class,
            1
        );
    }
    
}
