package evilcraft.block;


import evilcraft.EvilCraft;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;

/**
 * Config for {@link FluidBlockBlood}.
 * @author rubensworks
 *
 */
public class FluidBlockBloodConfig extends BlockConfig {
    
    /**
     * The unique instance.
     */
    public static FluidBlockBloodConfig _instance;

    /**
     * Make a new instance.
     */
    public FluidBlockBloodConfig() {
        super(
                EvilCraft._instance,
        	true,
            "blockBlood",
            null,
            FluidBlockBlood.class
        );
    }
    
    @Override
    public boolean isDisableable() {
        return false;
    }
    
}
