package evilcraft.block;


import evilcraft.EvilCraft;
import org.cyclops.cyclopscore.config.extendedconfig.BlockFluidConfig;

/**
 * Config for {@link FluidBlockPoison}.
 * @author rubensworks
 *
 */
public class FluidBlockPoisonConfig extends BlockFluidConfig {
    
    /**
     * The unique instance.
     */
    public static FluidBlockPoisonConfig _instance;

    /**
     * Make a new instance.
     */
    public FluidBlockPoisonConfig() {
        super(
                EvilCraft._instance,
        	true,
            "blockPoison",
            null,
            FluidBlockPoison.class
        );
    }
    
    @Override
    public boolean isDisableable() {
        return false;
    }
    
}
