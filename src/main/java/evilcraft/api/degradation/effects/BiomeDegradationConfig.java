package evilcraft.api.degradation.effects;

import evilcraft.Configs;
import evilcraft.api.config.DegradationEffectConfig;
import evilcraft.biomes.BiomeDegradedConfig;

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
