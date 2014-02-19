package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ItemBucketConfig;
import evilcraft.api.config.configurable.ConfigurableBlockFluidClassic;
import evilcraft.api.config.configurable.ConfigurableFluid;
import evilcraft.blocks.FluidBlockBlood;
import evilcraft.fluids.Blood;

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
    
}
