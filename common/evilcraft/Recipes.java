package evilcraft;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;
import evilcraft.blocks.DarkBlock;
import evilcraft.items.DarkGem;

/**
 * Holder class of all the recipes.
 */
public class Recipes {
    public static void registerRecipes() {
        // 9 DarkGems -> 1 DarkBlock
        GameRegistry.addRecipe(new ItemStack(DarkBlock.getInstance()), "xxx", "xxx", "xxx",
                'x', new ItemStack(DarkGem.getInstance()));
        // 1 DarkBlock -> 9 DarkGems
        GameRegistry.addShapelessRecipe(new ItemStack(DarkGem.getInstance(), 9), new ItemStack(DarkBlock.getInstance()));
    }
}
