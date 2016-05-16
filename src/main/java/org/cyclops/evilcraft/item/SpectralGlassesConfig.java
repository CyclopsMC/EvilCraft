package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Spectral Glasses.
 * @author rubensworks
 *
 */
public class SpectralGlassesConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static SpectralGlassesConfig _instance;

    /**
     * Make a new instance.
     */
    public SpectralGlassesConfig() {
        super(
                EvilCraft._instance,
        	true,
            "spectralGlasses",
            null,
            SpectralGlasses.class
        );
    }
    
}
