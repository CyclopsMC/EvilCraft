package evilcraft.api.degradation.effects;

import evilcraft.api.config.DegradationEffectConfig;
import evilcraft.biomes.BiomeDegraded;

/**
 * Config for {@link BiomeDegraded}.
 * @author rubensworks
 *
 */
public class TerraformDegradationConfig extends DegradationEffectConfig {
    
    /**
     * The unique instance.
     */
    public static TerraformDegradationConfig _instance;

    /**
     * Make a new instance.
     */
    public TerraformDegradationConfig() {
        super(
            false,
            "terraform",
            null,
            TerraformDegradation.class,
            1
        );
    }
    
}
