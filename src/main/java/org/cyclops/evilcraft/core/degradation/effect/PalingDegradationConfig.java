package org.cyclops.evilcraft.core.degradation.effect;

import org.cyclops.evilcraft.core.config.extendedconfig.DegradationEffectConfig;

/**
 * Config for {@link PalingDegradation}.
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
