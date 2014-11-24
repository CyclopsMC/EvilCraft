package evilcraft.item;

import evilcraft.block.FluidBlockBlood;
import evilcraft.core.config.configurable.ConfigurableBlockFluidClassic;
import evilcraft.core.config.configurable.ConfigurableFluid;
import evilcraft.core.config.configurable.ConfigurableItemBucket;
import evilcraft.core.config.configurable.IConfigurable;
import evilcraft.core.config.extendedconfig.ItemBucketConfig;
import evilcraft.fluid.Blood;

/**
 * Config for the Blood Bucket.
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
            null
        );
    }

    @Override
    protected IConfigurable initSubInstance() {
        return new ConfigurableItemBucket(this, FluidBlockBlood.getInstance());
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
