package evilcraft.item;

import evilcraft.block.FluidBlockBlood;
import evilcraft.core.config.configurable.ConfigurableBlockFluidClassic;
import evilcraft.core.config.configurable.ConfigurableFluid;
import evilcraft.core.config.extendedconfig.ItemBucketConfig;
import evilcraft.fluid.Blood;

/**
 * Config for the {@link BucketBlood}.
 * @author rubensworks
 *
 */
public class BucketBloodConfig extends ItemBucketConfig {
    
    /**
     * The unique instance.
     */
    public static BucketBloodConfig _instance;

    /**
     * Make a new instance.
     */
    public BucketBloodConfig() {
        super(
        	true,
            "bucketBlood",
            null,
            BucketBlood.class
        );
    }

    @Override
    public ConfigurableFluid getFluidInstance() {
        return Blood.getInstance();
    }

    @Override
    public ConfigurableBlockFluidClassic getFluidBlockInstance() {
        return FluidBlockBlood.getInstance();
    }
    
    @Override
    public boolean isDisableable() {
        return false;
    }
    
}
