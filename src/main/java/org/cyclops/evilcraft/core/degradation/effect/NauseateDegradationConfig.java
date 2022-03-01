package org.cyclops.evilcraft.core.degradation.effect;

import org.cyclops.evilcraft.core.config.extendedconfig.DegradationEffectConfig;

/**
 * Config for {@link NauseateDegradation}.
 * @author rubensworks
 *
 */
public class NauseateDegradationConfig extends DegradationEffectConfig {

    public NauseateDegradationConfig() {
        super(
                "nauseate",
                NauseateDegradation::new,
                3
        );
    }

}
