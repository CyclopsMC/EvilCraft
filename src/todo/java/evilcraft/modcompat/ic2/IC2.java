package evilcraft.modcompat.ic2;

import evilcraft.Configs;
import evilcraft.block.DarkOre;
import evilcraft.block.DarkOreConfig;
import evilcraft.item.DarkGem;
import evilcraft.item.DarkGemConfig;
import evilcraft.item.DarkGemCrushedConfig;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemStack;

/**
 * Call the IC2 API.
 * @author rubensworks
 */
public class IC2 {

    public static void registerMaceratorRecipes() {
        // Macerator dark ore
        if(Configs.isEnabled(DarkOreConfig.class) && Configs.isEnabled(DarkGemConfig.class)) {
            if(Configs.isEnabled(DarkGemCrushedConfig.class)) {
                Recipes.macerator.addRecipe(new RecipeInputItemStack(new ItemStack(DarkOre.getInstance())), null,
                        new ItemStack(DarkGem.getInstance(), 2), new ItemStack(DarkGemCrushedConfig._instance.getItemInstance(), 1));
            } else {
                Recipes.macerator.addRecipe(new RecipeInputItemStack(new ItemStack(DarkOre.getInstance())), null,
                        new ItemStack(DarkGem.getInstance(), 2));
            }
        }

        // Macerator dark ore -> crushed
        if(Configs.isEnabled(DarkGemConfig.class) && Configs.isEnabled(DarkGemCrushedConfig.class)) {
            Recipes.macerator.addRecipe(new RecipeInputItemStack(new ItemStack(DarkGem.getInstance())), null,
                    new ItemStack(DarkGemCrushedConfig._instance.getItemInstance(), 1));
        }
    }

}
