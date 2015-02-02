package evilcraft.infobook.pageelement;

import evilcraft.api.recipes.custom.IRecipe;
import evilcraft.block.BloodInfuser;
import evilcraft.client.gui.container.GuiOriginsOfDarkness;
import evilcraft.core.recipe.custom.DurationRecipeProperties;
import evilcraft.core.recipe.custom.ItemFluidStackAndTierRecipeComponent;
import evilcraft.core.recipe.custom.ItemStackRecipeComponent;
import evilcraft.item.BucketBloodConfig;
import evilcraft.item.Promise;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * Blood Infuser recipes.
 * @author rubensworks
 */
public class BloodInfuserRecipeAppendix extends RecipeAppendix<IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationRecipeProperties>> {

    private static final int SLOT_OFFSET_X = 16;
    private static final int SLOT_OFFSET_Y = 23;
    private static final int START_X_RESULT = 68;

    public BloodInfuserRecipeAppendix(IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationRecipeProperties> recipe) {
        super(recipe);
    }

    @Override
    protected int getWidth() {
        return START_X_RESULT + 32;
    }

    @Override
    protected int getHeightInner() {
        return 42;
    }

    @Override
    protected String getUnlocalizedTitle() {
        return "tile.blocks.bloodInfuser.name";
    }

    @Override
    public void drawElementInner(GuiOriginsOfDarkness gui, int x, int y, int width, int height, int page, int mx, int my) {
        int middle = (width - SLOT_SIZE) / 2;
        gui.drawArrowRight(x + middle - 3, y + SLOT_OFFSET_Y + 2);

        // Prepare items
        int tick = getTick(gui);
        ItemStack input = prepareItemStacks(recipe.getInput().getItemStacks(), tick);
        ItemStack result = prepareItemStack(recipe.getOutput().getItemStack(), tick);
        ItemStack promise = null;
        if(recipe.getInput().getTier() > 0) {
            promise = new ItemStack(Promise.getInstance(), 1, recipe.getInput().getTier() - 1);
        }

        // Items
        renderItem(gui, x + SLOT_OFFSET_X, y + SLOT_OFFSET_Y, input, mx, my);
        renderItem(gui, x + START_X_RESULT, y + SLOT_OFFSET_Y, result, mx, my);

        // Tier
        if(promise != null) {
            renderItem(gui, x + SLOT_OFFSET_X, y + 2, promise, mx, my);
        }

        renderIcon(gui, x + middle, y + 2, BucketBloodConfig._instance.getItemInstance().getIconFromDamage(0));
        renderItem(gui, x + middle, y + SLOT_OFFSET_Y, new ItemStack(BloodInfuser.getInstance()), mx, my, false);

        // Blood amount text
        FontRenderer fontRenderer = gui.getFontRenderer();
        boolean oldUnicode = fontRenderer.getUnicodeFlag();
        fontRenderer.setUnicodeFlag(true);
        fontRenderer.setBidiFlag(false);
        FluidStack fluidStack = recipe.getInput().getFluidStack();
        String line = fluidStack.amount + " mB";
        fontRenderer.drawSplitString(line, x + middle + SLOT_SIZE + 1, y + 6, 200, 0);
        fontRenderer.setUnicodeFlag(oldUnicode);
    }
}
