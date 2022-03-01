package org.cyclops.evilcraft.core.degradation.effect;

import org.cyclops.evilcraft.core.config.extendedconfig.DegradationEffectConfig;

/**
 * Config for {@link MobSpawnDegradation}.
 * @author rubensworks
 *
 */
public class MobSpawnDegradationConfig extends DegradationEffectConfig {

    public MobSpawnDegradationConfig() {
        super(
                "mobspawn",
                MobSpawnDegradation::new,
                2
        );
    }

}
