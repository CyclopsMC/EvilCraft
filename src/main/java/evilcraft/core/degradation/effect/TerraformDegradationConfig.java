package evilcraft.core.degradation.effect;

import evilcraft.core.config.extendedconfig.DegradationEffectConfig;
import evilcraft.world.biome.BiomeDegraded;

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
