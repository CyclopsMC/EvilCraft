package org.cyclops.evilcraft.modcompat.ic2;

import ic2.api.recipe.Recipes;
import ic2.core.recipe.RecipeInputItemStack;
import net.minecraft.item.ItemStack;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.block.DarkOre;
import org.cyclops.evilcraft.block.DarkOreConfig;
import org.cyclops.evilcraft.item.DarkGem;
import org.cyclops.evilcraft.item.DarkGemConfig;
import org.cyclops.evilcraft.item.DarkGemCrushedConfig;

/**
 * Call the IC2 API.
 * @author rubensworks
 */
public class IC2 {

    public static void registerMaceratorRecipes() {
        // Macerator dark ore
        if(Configs.isEnabled(DarkOreConfig.class) && Configs.isEnabled(DarkGemConfig.class)) {
            Recipes.macerator.addRecipe(new RecipeInputItemStack(new ItemStack(DarkOre.getInstance())), null, false,
                    new ItemStack(DarkGem.getInstance(), 2));
        }

        // Macerator dark ore -> crushed
        if(Configs.isEnabled(DarkGemConfig.class) && Configs.isEnabled(DarkGemCrushedConfig.class)) {
            Recipes.macerator.addRecipe(new RecipeInputItemStack(new ItemStack(DarkGem.getInstance())), null, false,
                    new ItemStack(DarkGemCrushedConfig._instance.getItemInstance(), 1));
        }
    }

}
