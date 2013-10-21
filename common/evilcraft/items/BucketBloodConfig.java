package evilcraft.items;

import evilcraft.api.config.ExtendedConfig;
import evilcraft.events.BloodBucketEventHook;
import evilcraft.liquids.Blood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidContainerRegistry;

public class BucketBloodConfig extends ExtendedConfig {
    
    public static BucketBloodConfig _instance;

    public BucketBloodConfig() {
        super(
            4004,
            "Blood Bucket",
            "bucketblood",
            null,
            BucketBlood.class
        );
    }
    
    @Override
    public void onRegistered() {
        MinecraftForge.EVENT_BUS.register(new BloodBucketEventHook());
        // Necessary?
        //FluidContainerRegistry.registerFluidContainer(Blood.getInstance(), new ItemStack(BucketBlood.getInstance()), FluidContainerRegistry.EMPTY_BUCKET);
    }
    
}
