package evilcraft.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.common.ICraftingHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import evilcraft.Reference;
import evilcraft.api.config.ElementTypeCategory;
import evilcraft.api.config.ItemConfig;
import evilcraft.api.config.configurable.ConfigurableProperty;
import evilcraft.fluids.Blood;

/**
 * Config for the {@link BloodContainer}.
 * @author rubensworks
 *
 */
public class BloodContainerConfig extends ItemConfig implements ICraftingHandler {
    
    /**
     * The unique instance.
     */
    public static BloodContainerConfig _instance;
    
    /**
     * Base container size in mB that will be multiplied every tier.
     */
    @ConfigurableProperty(category = ElementTypeCategory.GENERAL, comment = "The base amount of blood (mB) this container can hold * the level of container.")
    public static int containerSizeBase = 5000;
    
    /**
     * The different containers.
     */
    public static String[] containerLevelNames = {"Blood Cell", "Blood Can", "Blood Basin"};

    /**
     * Make a new instance.
     */
    public BloodContainerConfig() {
        super(
            Reference.ITEM_BLOODCONTAINER,
            "Blood Container",
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
        GameRegistry.registerCraftingHandler(this);
    }

    @Override
    public void onCrafting(EntityPlayer player, ItemStack item, IInventory craftMatrix) {
        if(item != null && item.getItem().getClass() == this.ELEMENT) {
            for(int i = 0; i < craftMatrix.getSizeInventory(); i++) {           
                if(craftMatrix.getStackInSlot(i) != null) {
                    ItemStack input = craftMatrix.getStackInSlot(i);
                    if(input.getItem() != null && input.getItem().getClass() == this.ELEMENT) {
                        FluidStack inputFluid = BloodContainer.getInstance().getFluid(input);
                        BloodContainer.getInstance().fill(item, inputFluid, true);
                    }
                }  
            }
        }
        
    }

    @Override
    public void onSmelting(EntityPlayer player, ItemStack item) {}
    
}
