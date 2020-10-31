package org.cyclops.evilcraft.core.degradation.effect;

import org.cyclops.evilcraft.core.config.extendedconfig.DegradationEffectConfig;

/**
 * Config for {@link ParticleDegradation}.
 * @author rubensworks
 *
 */
public class ParticleDegradationConfig extends DegradationEffectConfig {

    public ParticleDegradationConfig() {
        super(
                "particle",
                ParticleDegradation::new,
                10
        );
    }

}
