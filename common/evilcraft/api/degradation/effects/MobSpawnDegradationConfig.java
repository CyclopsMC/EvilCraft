package evilcraft.api.degradation.effects;

import evilcraft.api.config.DegradationEffectConfig;

/**
 * Config for {@link MobSpawnDegradation}.
 * @author rubensworks
 *
 */
public class MobSpawnDegradationConfig extends DegradationEffectConfig {
    
    /**
     * The unique instance.
     */
    public static MobSpawnDegradationConfig _instance;

    /**
     * Make a new instance.
     */
    public MobSpawnDegradationConfig() {
        super(
            true,
            "mobSpawn",
            null,
            MobSpawnDegradation.class,
            2
        );
    }
    
}
