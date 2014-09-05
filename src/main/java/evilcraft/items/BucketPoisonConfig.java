package evilcraft.items;

import evilcraft.blocks.FluidBlockPoison;
import evilcraft.core.config.ItemBucketConfig;
import evilcraft.core.config.configurable.ConfigurableBlockFluidClassic;
import evilcraft.core.config.configurable.ConfigurableFluid;
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
    
    @Override
    public boolean isDisableable() {
        return false;
    }
    
}
