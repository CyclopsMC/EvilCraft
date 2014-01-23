package evilcraft.items;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import evilcraft.Reference;
import evilcraft.api.config.ElementTypeCategory;
import evilcraft.api.config.ItemConfig;
import evilcraft.api.config.configurable.ConfigurableProperty;
import evilcraft.fluids.Blood;

public class BloodExtractorConfig extends ItemConfig {
    
    public static BloodExtractorConfig _instance;
    
    @ConfigurableProperty(category = ElementTypeCategory.GENERAL, comment = "The minimum amount of blood (mB) that can be extracted from this block.")
    public static int minMB = 1000;
    @ConfigurableProperty(category = ElementTypeCategory.GENERAL, comment = "The maximum amount of blood (mB) that can be extracted from this block. IMPORTANT: must be larger than minMB!")
    public static int maxMB = 2000;
    @ConfigurableProperty(category = ElementTypeCategory.GENERAL, comment = "The amount of blood (mB) this container can hold.")
    public static int containerSize = 5000;

    public BloodExtractorConfig() {
        super(
            Reference.ITEM_BLOODEXTRACTOR,
            "Blood Extractor",
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
