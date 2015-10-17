package evilcraft.item;

import evilcraft.core.config.extendedconfig.ItemConfig;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.fluid.Blood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * Config for the Dull Dust.
 * @author rubensworks
 *
 */
public class CondensedBloodConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static CondensedBloodConfig _instance;

    /**
     * Make a new instance.
     */
    public CondensedBloodConfig() {
        super(
        	true,
            "condensedBlood",
            null,
            null
        );
    }

    @Override
    public void onRegistered() {
        // Register fluid container
        FluidStack fluidStack = new FluidStack(Blood.getInstance(), 500);
        ItemStack filledContainer = new ItemStack(getItemInstance());
        ItemStack emptyContainer = filledContainer.copy();
        emptyContainer.stackSize--;
        FluidContainerRegistry.registerFluidContainer(fluidStack, filledContainer, emptyContainer);

        // Register in loot chests
        for(String chestCategory : MinecraftHelpers.CHESTGENCATEGORIES) {
            ChestGenHooks.getInfo(chestCategory).addItem(new WeightedRandomChestContent(
                    getItemInstance(), 0, 5, 32, 10));
        }
    }
    
}
