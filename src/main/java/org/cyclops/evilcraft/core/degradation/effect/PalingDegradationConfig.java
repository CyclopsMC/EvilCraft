package org.cyclops.evilcraft.core.degradation.effect;

import org.cyclops.evilcraft.core.config.extendedconfig.DegradationEffectConfig;

/**
 * Config for {@link PalingDegradation}.
 * @author rubensworks
 *
 */
public class PalingDegradationConfig extends DegradationEffectConfig {

    public PalingDegradationConfig() {
        super(
                "paling",
                PalingDegradation::new,
                1
        );
    }

}
