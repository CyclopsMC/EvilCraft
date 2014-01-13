package evilcraft;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;

public class CustomRecipeRegistry {
    
    private static final Map<CustomRecipe, CustomRecipeResult> recipes = new HashMap<CustomRecipe, CustomRecipeResult>();
    
    public static void put(CustomRecipe recipe, ItemStack result) {
        recipes.put(recipe, new CustomRecipeResult(recipe, result));
    }
    
    public static CustomRecipeResult get(CustomRecipe recipe) {
        // TODO: when the recipes list gets big, this might need to get more efficient...
        for(Entry<CustomRecipe, CustomRecipeResult> entry : recipes.entrySet()) {
            if(entry.getKey().equals(recipe))
                return entry.getValue();
        }
        return null;
    }
}
