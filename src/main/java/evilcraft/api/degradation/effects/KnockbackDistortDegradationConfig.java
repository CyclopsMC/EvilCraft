package evilcraft.api.degradation.effects;

import evilcraft.api.config.DegradationEffectConfig;

/**
 * Config for {@link KnockbackDistortDegradation}.
 * @author rubensworks
 *
 */
public class KnockbackDistortDegradationConfig extends DegradationEffectConfig {
    
    /**
     * The unique instance.
     */
    public static KnockbackDistortDegradationConfig _instance;

    /**
     * Make a new instance.
     */
    public KnockbackDistortDegradationConfig() {
        super(
            true,
            "knockbackDistort",
            null,
            KnockbackDistortDegradation.class,
            5
        );
    }
    
}
