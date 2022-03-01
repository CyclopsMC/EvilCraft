package org.cyclops.evilcraft.core.degradation.effect;

import org.cyclops.evilcraft.core.config.extendedconfig.DegradationEffectConfig;

/**
 * Config for {@link BiomeDegradation}.
 * @author rubensworks
 *
 */
public class BiomeDegradationConfig extends DegradationEffectConfig {

    public BiomeDegradationConfig() {
        super(
                "biome",
                BiomeDegradation::new,
                1
        );
    }

}
