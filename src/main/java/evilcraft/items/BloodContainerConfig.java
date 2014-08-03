package evilcraft.items;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import evilcraft.api.config.ElementTypeCategory;
import evilcraft.api.config.ItemConfig;
import evilcraft.api.config.configurable.ConfigurableProperty;
import evilcraft.fluids.Blood;

/**
 * Config for the {@link BloodContainer}.
 * @author rubensworks
 *
 */
public class BloodContainerConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static BloodContainerConfig _instance;
    
    /**
     * Base container size in mB that will be multiplied every tier.
     */
    @ConfigurableProperty(category = ElementTypeCategory.GENERAL, comment = "The base amount of blood (mB) this container can hold * the level of container.", requiresMcRestart = true)
    public static int containerSizeBase = 5000;
    
    /**
     * The different containers.
     */
    public static String[] containerLevelNames = {"bloodCell", "bloodCan", "bloodBasin", "creativeBloodContainer"};

    /**
     * Make a new instance.
     */
    public BloodContainerConfig() {
        super(
        	true,
            "bloodContainer",
            null,
            BloodContainer.class
        );
    }
    
    /**
     * Get the amount of container tiers.
     * @return The container tiers.
     */
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
