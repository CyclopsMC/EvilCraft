package org.cyclops.evilcraft.core.degradation.effect;

import org.cyclops.evilcraft.core.config.extendedconfig.DegradationEffectConfig;

/**
 * Config for the ødegradation sound.
 * @author rubensworks
 *
 */
public class SoundDegradationConfig extends DegradationEffectConfig {

    public SoundDegradationConfig() {
        super(
                "sound",
                SoundDegradation::new,
                3
        );
    }

}
