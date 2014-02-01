package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ItemBucketConfig;
import evilcraft.api.config.configurable.ConfigurableBlockFluidClassic;
import evilcraft.api.config.configurable.ConfigurableFluid;
import evilcraft.blocks.FluidBlockPoison;
import evilcraft.fluids.Poison;

public class BucketPoisonConfig extends ItemBucketConfig {
    
    public static BucketPoisonConfig _instance;

    public BucketPoisonConfig() {
        super(
            Reference.ITEM_BUCKETPOISON,
            "Poison Bucket",
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
