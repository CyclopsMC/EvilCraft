package evilcraft.items;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import evilcraft.api.config.ElementTypeCategory;
import evilcraft.api.config.ItemConfig;
import evilcraft.api.config.configurable.ConfigurableProperty;
import evilcraft.fluids.Blood;

/**
 * Config for the {@link BloodExtractor}.
 * @author rubensworks
 *
 */
public class BloodExtractorConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static BloodExtractorConfig _instance;
    
    /**
     * The minimum amount of blood (mB) that can be extracted from this block.
     */
    @ConfigurableProperty(category = ElementTypeCategory.GENERAL, comment = "The minimum amount of blood (mB) that can be extracted from this block.")
    public static int minMB = 250;
    /**
     * The maximum amount of blood (mB) that can be extracted from this block.
     */
    @ConfigurableProperty(category = ElementTypeCategory.GENERAL, comment = "The maximum amount of blood (mB) that can be extracted from this block. IMPORTANT: must be larger than minMB!")
    public static int maxMB = 750;
    /**
     * The minimum multiplier for amount of mB to receive per mob HP.
     */
    @ConfigurableProperty(category = ElementTypeCategory.GENERAL, comment = "The minimum multiplier for amount of mB to receive per mob HP.")
    public static float minimumMobMultiplier = 5;
    /**
     * The minimum multiplier for amount of mB to receive per mob HP.
     */
    @ConfigurableProperty(category = ElementTypeCategory.GENERAL, comment = "The maximum multiplier for amount of mB to receive per mob HP. IMPORTANT: must be larger than maximumMobMultiplier!")
    public static float maximumMobMultiplier = 40;
    /**
     * The amount of blood (mB) this container can hold.
     */
    @ConfigurableProperty(category = ElementTypeCategory.GENERAL, comment = "The amount of blood (mB) this container can hold.")
    public static int containerSize = 5000;

    /**
     * Make a new instance.
     */
    public BloodExtractorConfig() {
        super(
        	true,
            "bloodExtractor",
            null,
            BloodExtractor.class
        );
    }
    
    @Override
    public void onRegistered() {
        FluidContainerRegistry.registerFluidContainer(
                FluidRegistry.getFluidStack(Blood.getInstance().getName(), containerSize),
                new ItemStack(BloodExtractor.getInstance())
        );
    }
    
}
