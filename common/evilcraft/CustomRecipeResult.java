package evilcraft;

import net.minecraft.item.ItemStack;

public class CustomRecipeResult {
    private CustomRecipe recipe;
    private ItemStack result;
    
    public CustomRecipeResult(CustomRecipe recipe, ItemStack result) {
        this.recipe = recipe;
        this.result = result;
    }
    
    public CustomRecipe getRecipe() {
        return recipe;
    }
    public ItemStack getResult() {
        return result;
    }
    
}
