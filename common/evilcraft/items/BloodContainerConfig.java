package evilcraft.items;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import evilcraft.Reference;
import evilcraft.api.config.ElementTypeCategory;
import evilcraft.api.config.ItemConfig;
import evilcraft.api.config.configurable.ConfigurableProperty;
import evilcraft.fluids.Blood;

public class BloodContainerConfig extends ItemConfig {
    
    public static BloodContainerConfig _instance;
    
    @ConfigurableProperty(category = ElementTypeCategory.GENERAL, comment = "The base amount of blood (mB) this container can hold * the level of container.")
    public static int containerSizeBase = 5000;
    
    public static String[] containerLevelNames = {"Blood Cell", "Blood Can", "Blood Basin"};

    public BloodContainerConfig() {
        super(
            Reference.ITEM_BLOODCONTAINER,
            "Blood Container",
            "bloodContainer",
            null,
            BloodContainer.class
        );
    }
    
    public static int getContainerLevels() {
        return containerLevelNames.length;
    }
    
    @Override
    public void onRegistered() {
        for(int level = 0; level < getContainerLevels(); level ++) {
            ItemStack itemStack = new ItemStack(BloodContainer.getInstance(), 1, level);
            FluidContainerRegistry.registerFluidContainer(
                    FluidRegistry.getFluidStack(Blood.getInstance().getName(), BloodContainer.getInstance().getCapacity(itemStack)),
                    itemStack
            );
        }
    }
    
}
