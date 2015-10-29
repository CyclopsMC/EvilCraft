package org.cyclops.evilcraft.infobook.pageelement;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.infobook.AdvancedButton;
import org.cyclops.cyclopscore.infobook.GuiInfoBook;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.InfoSection;
import org.cyclops.cyclopscore.infobook.pageelement.RecipeAppendix;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.cyclopscore.recipe.custom.component.ItemStackRecipeComponent;
import org.cyclops.evilcraft.block.BloodInfuser;
import org.cyclops.evilcraft.core.recipe.custom.DurationXpRecipeProperties;
import org.cyclops.evilcraft.core.recipe.custom.ItemFluidStackAndTierRecipeComponent;
import org.cyclops.evilcraft.item.BucketBloodConfig;
import org.cyclops.evilcraft.item.Promise;

/**
 * Blood Infuser recipes.
 * @author rubensworks
 */
public class BloodInfuserRecipeAppendix extends RecipeAppendix<IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationXpRecipeProperties>> {

    private static final int SLOT_OFFSET_X = 16;
    private static final int SLOT_OFFSET_Y = 23;
    private static final int START_X_RESULT = 68;

    private static final AdvancedButton.Enum INPUT = AdvancedButton.Enum.create();
    private static final AdvancedButton.Enum RESULT = AdvancedButton.Enum.create();
    private static final AdvancedButton.Enum PROMISE = AdvancedButton.Enum.create();

    public BloodInfuserRecipeAppendix(IInfoBook infoBook, IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationXpRecipeProperties> recipe) {
        super(infoBook, recipe);
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
        return "tile.blocks.evilcraft.bloodInfuser.name";
    }

    @Override
    public void bakeElement(InfoSection infoSection) {
        renderItemHolders.put(INPUT, new ItemButton());
        renderItemHolders.put(RESULT, new ItemButton());
        renderItemHolders.put(PROMISE, new ItemButton());
        super.bakeElement(infoSection);
    }

    @Override
    public void drawElementInner(GuiInfoBook gui, int x, int y, int width, int height, int page, int mx, int my) {
        int middle = (width - SLOT_SIZE) / 2;
        gui.drawArrowRight(x + middle - 3, y + SLOT_OFFSET_Y + 2);

        // Prepare items
        int tick = getTick(gui);
        ItemStack input = prepareItemStacks(recipe.getInput().getItemStacks(), tick);
        ItemStack result = prepareItemStacks(recipe.getOutput().getItemStacks(), tick);
        ItemStack promise = null;
        if(recipe.getInput().getTier() > 0) {
            promise = new ItemStack(Promise.getInstance(), 1, recipe.getInput().getTier() - 1);
        }

        // Items
        renderItem(gui, x + SLOT_OFFSET_X, y + SLOT_OFFSET_Y, input, mx, my, INPUT);
        renderItem(gui, x + START_X_RESULT, y + SLOT_OFFSET_Y, result, mx, my, RESULT);

        // Tier
        if(promise != null) {
            renderItem(gui, x + SLOT_OFFSET_X, y + 2, promise, mx, my, PROMISE);
        }

        renderItem(gui, x + middle, y + 2, new ItemStack(BucketBloodConfig._instance.getItemInstance()), mx, my, false, null);
        renderItem(gui, x + middle, y + SLOT_OFFSET_Y, new ItemStack(BloodInfuser.getInstance()), mx, my, false, null);

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
