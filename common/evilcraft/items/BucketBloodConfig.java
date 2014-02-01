package evilcraft.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import evilcraft.Reference;
import evilcraft.api.BucketHandler;
import evilcraft.api.config.ItemBucketConfig;
import evilcraft.api.config.configurable.ConfigurableBlockFluidClassic;
import evilcraft.api.config.configurable.ConfigurableFluid;
import evilcraft.blocks.FluidBlockBlood;
import evilcraft.fluids.Blood;

public class BucketBloodConfig extends ItemBucketConfig {
    
    public static BucketBloodConfig _instance;

    public BucketBloodConfig() {
        super(
            Reference.ITEM_BUCKETBLOOD,
            "Blood Bucket",
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
