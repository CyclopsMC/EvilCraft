package org.cyclops.evilcraft.core.degradation.effect;

import org.cyclops.evilcraft.core.config.extendedconfig.DegradationEffectConfig;

/**
 * Config for {@link NauseateDegradation}.
 * @author rubensworks
 *
 */
public class NauseateDegradationConfig extends DegradationEffectConfig {
    
    /**
     * The unique instance.
     */
    public static NauseateDegradationConfig _instance;

    /**
     * Make a new instance.
     */
    public NauseateDegradationConfig() {
        super(
            true,
            "nauseate",
            null,
            NauseateDegradation.class,
            3
        );
    }
    
}
