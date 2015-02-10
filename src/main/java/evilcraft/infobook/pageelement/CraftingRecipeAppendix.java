package evilcraft.infobook.pageelement;

import evilcraft.EvilCraft;
import evilcraft.client.gui.container.GuiOriginsOfDarkness;
import evilcraft.infobook.AdvancedButton;
import evilcraft.infobook.InfoSection;
import net.minecraft.init.Blocks;
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

    private static final int SLOT_OFFSET_X = 5;
    private static final int SLOT_OFFSET_Y = 5;
    private static final int START_X_RESULT = 84;

    private static final AdvancedButton.Enum[] INPUT = new AdvancedButton.Enum[9];
    static {
        for(int i = 0; i < 9; i++) INPUT[i] = AdvancedButton.Enum.create();
    }
    private static final AdvancedButton.Enum RESULT = AdvancedButton.Enum.create();

    public CraftingRecipeAppendix(IRecipe recipe) {
        super(recipe);
    }

    @Override
    protected int getWidth() {
        return START_X_RESULT + 20;
    }

    @Override
    protected int getHeightInner() {
        return 58;
    }

    @Override
    protected String getUnlocalizedTitle() {
        return "tile.workbench.name";
    }

    @Override
    public void bakeElement(InfoSection infoSection) {
        for(int i = 0; i < 9; i++) renderItemHolders.put(INPUT[i], new ItemButton());
        renderItemHolders.put(RESULT, new ItemButton());
        super.bakeElement(infoSection);
    }

    @Override
    protected void drawElementInner(GuiOriginsOfDarkness gui, int x, int y, int width, int height, int page, int mx, int my) {
        gui.drawArrowRight(x + (SLOT_SIZE + SLOT_OFFSET_X) * 3 - 3, y + SLOT_OFFSET_Y + SLOT_SIZE + 2);

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
                renderItem(gui, x + (SLOT_SIZE + SLOT_OFFSET_X) * i, y+ (SLOT_SIZE + SLOT_OFFSET_Y) * j,
                        grid[i + j * 3], mx, my, INPUT[i + j * 3]);
            }
        }
        renderItem(gui, x + START_X_RESULT, y + (SLOT_SIZE + SLOT_OFFSET_Y), result, mx, my, RESULT);

        // Crafting Table icon
        renderItem(gui, x + (SLOT_SIZE + SLOT_OFFSET_X) * 3, y + SLOT_OFFSET_Y + SLOT_SIZE,
                new ItemStack(Blocks.crafting_table), mx, my, false, null);
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
        if(element == null) return Collections.EMPTY_LIST;
        return element instanceof ItemStack ? Arrays.asList((ItemStack) element) : (List<ItemStack>) element;
    }

}
