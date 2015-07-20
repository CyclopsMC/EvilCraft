package org.cyclops.evilcraft.item;

import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockFluidClassic;
import org.cyclops.cyclopscore.config.configurable.ConfigurableFluid;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItemBucket;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ItemBucketConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.block.FluidBlockBlood;
import org.cyclops.evilcraft.fluid.Blood;

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
                EvilCraft._instance,
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
