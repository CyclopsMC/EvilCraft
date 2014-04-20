package evilcraft.items;

import evilcraft.api.config.ItemConfig;

/**
 * Config for the {@link MaceOfDistortion}.
 * @author rubensworks
 *
 */
public class MaceOfDistortionConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static MaceOfDistortionConfig _instance;

    /**
     * Make a new instance.
     */
    public MaceOfDistortionConfig() {
        super(
            true,
            "maceOfDistortion",
            null,
            MaceOfDistortion.class
        );
    }
    
}
