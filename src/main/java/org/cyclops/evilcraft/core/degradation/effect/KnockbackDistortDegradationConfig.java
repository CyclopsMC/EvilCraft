package org.cyclops.evilcraft.core.degradation.effect;

import org.cyclops.evilcraft.core.config.extendedconfig.DegradationEffectConfig;

/**
 * Config for {@link KnockbackDistortDegradation}.
 * @author rubensworks
 *
 */
public class KnockbackDistortDegradationConfig extends DegradationEffectConfig {

    public KnockbackDistortDegradationConfig() {
        super(
                "knockbackdistort",
                KnockbackDistortDegradation::new,
                5
        );
    }

}
