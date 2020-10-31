package org.cyclops.evilcraft.core.degradation.effect;

import org.cyclops.evilcraft.core.config.extendedconfig.DegradationEffectConfig;
import org.cyclops.evilcraft.world.biome.BiomeDegraded;

/**
 * Config for {@link BiomeDegraded}.
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
