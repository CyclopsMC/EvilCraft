package evilcraft.core.degradation.effects;

import evilcraft.biomes.BiomeDegraded;
import evilcraft.core.config.DegradationEffectConfig;

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
