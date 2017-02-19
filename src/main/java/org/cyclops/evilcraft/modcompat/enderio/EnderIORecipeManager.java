package org.cyclops.evilcraft.modcompat.enderio;

import net.minecraft.item.ItemStack;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.block.DarkOre;
import org.cyclops.evilcraft.item.DarkGem;
import org.cyclops.evilcraft.item.DarkGemConfig;
import org.cyclops.evilcraft.item.DarkGemCrushedConfig;

import crazypants.enderio.machine.sagmill.SagMillRecipeManager;

/**
 * EnderIO recipe manager registrations.
 * @author runesmacher
 *
 */
public class EnderIORecipeManager{
    public static void register() {
        // Sagmill dark ore
        if(Configs.isEnabled(DarkGemConfig.class) && Configs.isEnabled(DarkGemCrushedConfig.class)) {
            ItemStack input = new ItemStack(DarkOre.getInstance());
            ItemStack output = new ItemStack(DarkGem.getInstance(), 2);
            int energy = 3600;

            SagMillRecipeManager.getInstance().addRecipe(input, energy, output);
        }

        // Sagmill dark gem -> crushed dark gem
        if(Configs.isEnabled(DarkGemConfig.class) && Configs.isEnabled(DarkGemCrushedConfig.class)) {
            ItemStack input = new ItemStack(DarkGem.getInstance());
            ItemStack output = new ItemStack(DarkGemCrushedConfig._instance.getItemInstance(), 1);
            int energy = 2400;

            SagMillRecipeManager.getInstance().addRecipe(input, energy, output);
        }
    }

}
