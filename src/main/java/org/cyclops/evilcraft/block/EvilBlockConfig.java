package org.cyclops.evilcraft.block;


import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link EvilBlock}.
 * @author rubensworks
 *
 */
public class EvilBlockConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static EvilBlockConfig _instance;

    /**
     * Make a new instance.
     */
    public EvilBlockConfig() {
        super(
                EvilCraft._instance,
        	true,
            "evilBlock",
            null,
            EvilBlock.class
        );
    }
    
    @Override
    public boolean isHardDisabled() {
    	return true;
    }
    
}
