package org.cyclops.evilcraft.block;


import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * A config for {@link HardenedBlood}.
 * @author rubensworks
 *
 */
public class HardenedBloodConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static HardenedBloodConfig _instance;

    /**
     * Make a new instance.
     */
    public HardenedBloodConfig() {
        super(
                EvilCraft._instance,
        	true,
            "hardenedBlood",
            null,
            HardenedBlood.class
        );
    }
    
    @Override
    public boolean isMultipartEnabled() {
        return true;
    }
    
    @Override
    public boolean isDisableable() {
        return false;
    }
    
}
