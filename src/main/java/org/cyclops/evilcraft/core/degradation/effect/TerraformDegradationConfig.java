package org.cyclops.evilcraft.core.degradation.effect;

import org.cyclops.evilcraft.core.config.extendedconfig.DegradationEffectConfig;

/**
 * Config for terraform degradation.
 * @author rubensworks
 *
 */
public class TerraformDegradationConfig extends DegradationEffectConfig {

    public TerraformDegradationConfig() {
        super(
                "terraform",
                TerraformDegradation::new,
                1
        );
    }

}
