package org.cyclops.evilcraft.core.degradation.effect;

import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.core.config.extendedconfig.DegradationEffectConfig;
import org.cyclops.evilcraft.world.biome.BiomeDegradedConfig;

/**
 * Config for {@link BiomeDegradation}.
 * @author rubensworks
 *
 */
public class BiomeDegradationConfig extends DegradationEffectConfig {
    
    /**
     * The unique instance.
     */
    public static BiomeDegradationConfig _instance;

    /**
     * Make a new instance.
     */
    public BiomeDegradationConfig() {
        super(
            true,
            "biome",
            null,
            BiomeDegradation.class,
            1
        );
    }
    
    @Override
    public boolean isEnabled() {
        return super.isEnabled() && Configs.isEnabled(BiomeDegradedConfig.class);
    }
    
}
