package evilcraft.core.helper;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

/**
 * Several convenience functions for crafting.
 * @author rubensworks
 */
public class CraftingHelpers {

    public static IRecipe findShapedRecipe(ItemStack itemStack, int index) {
        int indexAttempt = index;
        for(IRecipe recipe : (List<IRecipe>) CraftingManager.getInstance().getRecipeList()) {
            if(itemStacksEqual(recipe.getRecipeOutput(), itemStack) && indexAttempt-- == 0) {
                return recipe;
            }
        }
        throw new IllegalArgumentException("Could not find crafting recipe for " + itemStack.getItem().getUnlocalizedName() +
                "with index " + index);
    }

    public static boolean itemStacksEqual(ItemStack itemStack1, ItemStack itemStack2) {
        return itemStack1 != null && itemStack2 != null &&
               itemStack1.getItem() == itemStack2.getItem() &&
               (itemStack1.getItemDamage() == itemStack2.getItemDamage() ||
                       itemStack1.getItemDamage() == OreDictionary.WILDCARD_VALUE ||
                       itemStack2.getItemDamage() == OreDictionary.WILDCARD_VALUE ||
                       itemStack1.getItem().isDamageable());
    }

}
