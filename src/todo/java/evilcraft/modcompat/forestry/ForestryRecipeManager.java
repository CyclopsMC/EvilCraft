package evilcraft.modcompat.forestry;

import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.block.UndeadLeaves;
import org.cyclops.evilcraft.block.UndeadLeavesConfig;
import org.cyclops.evilcraft.block.UndeadLogConfig;
import org.cyclops.evilcraft.fluid.Blood;
import org.cyclops.evilcraft.fluid.BloodConfig;
import org.cyclops.evilcraft.item.HardenedBloodShardConfig;
import forestry.api.recipes.RecipeManagers;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * Forestry recipe manager registrations.
 * @author rubensworks
 *
 */
public class ForestryRecipeManager {

    /**
     * Register {@link RecipeManagers} calls.
     */
    public static void register() {
     // Register Undead Log squeezer recipe.
        if(Configs.isEnabled(UndeadLogConfig.class)
                && Configs.isEnabled(BloodConfig.class)
                && Configs.isEnabled(HardenedBloodShardConfig.class)) {
            int time = 20;
            ItemStack[] input = {new ItemStack(UndeadLogConfig._instance.getBlockInstance())};
            FluidStack fluidStack = new FluidStack(Blood.getInstance(),
                    FluidContainerRegistry.BUCKET_VOLUME / 10);
            ItemStack output = new ItemStack(HardenedBloodShardConfig._instance.getItemInstance());
            int outputChance = 25; // Out of 100
            RecipeManagers.squeezerManager.addRecipe(time, input, fluidStack, output, outputChance);
        }
        
        // Register Undead Leaves squeezer recipes.
        if(Configs.isEnabled(UndeadLeavesConfig.class)
                && Configs.isEnabled(BloodConfig.class)
                && Configs.isEnabled(HardenedBloodShardConfig.class)) {
            int time = 10;
            ItemStack[] input = {new ItemStack(UndeadLeaves.getInstance())};
            FluidStack fluidStack = new FluidStack(Blood.getInstance(),
                    FluidContainerRegistry.BUCKET_VOLUME / 20);
            ItemStack output = new ItemStack(HardenedBloodShardConfig._instance.getItemInstance());
            int outputChance = 10; // Out of 100
            RecipeManagers.squeezerManager.addRecipe(time, input, fluidStack, output, outputChance);
        }
    }
    
}
