package evilcraft.infobook.pageelement;

import evilcraft.EvilCraft;
import evilcraft.client.gui.container.GuiOriginsOfDarkness;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.apache.logging.log4j.Level;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Shaped recipes.
 * @author rubensworks
 */
public class CraftingRecipeAppendix extends RecipeAppendix<IRecipe> {

    private static final int START_X = 22;
    private static final int START_Y = 0;
    private static final int OFFSET_X = 5;
    private static final int OFFSET_Y = 5;
    private static final int START_X_RESULT = 104;

    public CraftingRecipeAppendix(IRecipe recipe) {
        super(recipe);
    }

    @Override
    public void drawScreen(GuiOriginsOfDarkness gui, int x, int y, int width, int height, int page, int mx, int my) {
        gui.drawOuterBorder(x + START_X - 3, y - 1, START_X_RESULT - START_X + 22, getHeight() + 6, 0.5F, 0.5F, 0.5F, 0.4f);

        // Prepare items
        int tick = getTick(gui);
        ItemStack[] grid = new ItemStack[9];
        ItemStack result = prepareItemStack(recipe.getRecipeOutput(), tick);
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                grid[i + j * 3] = prepareItemStacks(getItemStacks(i + j * 3), tick);
            }
        }

        // Items
        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                renderItem(gui, x + START_X + (SLOT_SIZE + OFFSET_X) * i, y + START_Y + (SLOT_SIZE + OFFSET_Y) * j,
                        grid[i + j * 3], mx, my);
            }
        }
        renderItem(gui, x + START_X_RESULT, y + START_Y + (SLOT_SIZE + OFFSET_Y), result, mx, my);

        // Tooltips
        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                renderItemTooltip(gui, x + START_X + (SLOT_SIZE + OFFSET_X) * i, y + START_Y + (SLOT_SIZE + OFFSET_Y) * j,
                        grid[i + j * 3], mx, my);
            }
        }
        renderItemTooltip(gui, x + START_X_RESULT, y + START_Y + (SLOT_SIZE + OFFSET_Y), result, mx, my);
    }

    protected List<ItemStack> getItemStacks(int index) {
        Object[] itemStacks;
        if(recipe instanceof ShapedRecipes) {
            itemStacks = ((ShapedRecipes) recipe).recipeItems;
        } else if(recipe instanceof ShapedOreRecipe) {
            itemStacks = ((ShapedOreRecipe) recipe).getInput();
        } else if(recipe instanceof ShapelessRecipes) {
            itemStacks = ((ShapelessRecipes) recipe).recipeItems.toArray();
        } else if(recipe instanceof ShapelessOreRecipe) {
            itemStacks = ((ShapelessOreRecipe) recipe).getInput().toArray();
        } else {
            EvilCraft.log("Recipe of type " + recipe.getClass() + " is not supported.", Level.ERROR);
            return Collections.EMPTY_LIST;
        }
        if(itemStacks.length <= index) return Collections.EMPTY_LIST;
        Object element = itemStacks[index];
        return element instanceof ItemStack ? Arrays.asList((ItemStack) element) : (List<ItemStack>) element;
    }

}
