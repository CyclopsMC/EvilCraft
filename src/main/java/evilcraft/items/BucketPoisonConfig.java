package evilcraft.items;

import evilcraft.api.config.ItemBucketConfig;
import evilcraft.api.config.configurable.ConfigurableBlockFluidClassic;
import evilcraft.api.config.configurable.ConfigurableFluid;
import evilcraft.blocks.FluidBlockPoison;
import evilcraft.fluids.Poison;

/**
 * Config for the {@link BucketPoison}.
 * @author rubensworks
 *
 */
public class BucketPoisonConfig extends ItemBucketConfig {
    
    /**
     * The unique instance.
     */
    public static BucketPoisonConfig _instance;

    /**
     * Make a new instance.
     */
    public BucketPoisonConfig() {
        super(
        	true,
            "bucketPoison",
            null,
            BucketPoison.class
        );
    }

    @Override
    public ConfigurableFluid getFluidInstance() {
        return Poison.getInstance();
    }

    @Override
    public ConfigurableBlockFluidClassic getFluidBlockInstance() {
        return FluidBlockPoison.getInstance();
    }
    
}
