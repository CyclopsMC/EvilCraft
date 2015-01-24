package evilcraft.infobook.pageelement;

import evilcraft.api.recipes.custom.IRecipe;
import evilcraft.client.gui.container.GuiOriginsOfDarkness;
import evilcraft.core.recipe.custom.DurationRecipeProperties;
import evilcraft.core.recipe.custom.ItemFluidStackAndTierRecipeComponent;
import evilcraft.core.recipe.custom.ItemStackRecipeComponent;
import net.minecraft.item.ItemStack;

/**
 * Blood Infuser recipes.
 * @author rubensworks
 */
public class BloodInfuserRecipeAppendix extends RecipeAppendix<IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationRecipeProperties>> {

    private static final int START_X = 43;
    private static final int START_Y = 0;
    private static final int START_X_RESULT = 104;

    public BloodInfuserRecipeAppendix(IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationRecipeProperties> recipe) {
        super(recipe);
    }

    @Override
    public void drawScreen(GuiOriginsOfDarkness gui, int x, int y, int width, int height, int page, int mx, int my) {
        // TODO: render border in super; also define width to allow auto-centering instead of X_OFFSET
        gui.drawOuterBorder(x + START_X - 3, y - 1, START_X_RESULT - START_X + 22, getHeight() + 6, 0.5F, 0.5F, 0.5F, 0.4f);

        // Prepare items
        int tick = getTick(gui);
        ItemStack input = prepareItemStacks(recipe.getInput().getItemStacks(), tick);
        ItemStack result = prepareItemStack(recipe.getOutput().getItemStack(), tick);

        // Items
        renderItem(gui, x + START_X, y + START_Y, input, mx, my);
        renderItem(gui, x + START_X_RESULT, y + START_Y, result, mx, my);

        // Tier
        // TODO

        // Tank
        // TODO

        // Tooltips
        renderItemTooltip(gui, x + START_X, y + START_Y, input, mx, my);
        renderItemTooltip(gui, x + START_X_RESULT, y + START_Y, result, mx, my);
    }
}
