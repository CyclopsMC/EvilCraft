package org.cyclops.evilcraft.infobook.pageelement;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.infobook.AdvancedButtonEnum;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.InfoSection;
import org.cyclops.cyclopscore.infobook.ScreenInfoBook;
import org.cyclops.cyclopscore.infobook.pageelement.RecipeAppendix;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.core.helper.ItemHelpers;
import org.cyclops.evilcraft.core.recipe.type.RecipeBloodInfuser;
import org.cyclops.evilcraft.item.ItemPromise;

/**
 * Blood Infuser recipes.
 * @author rubensworks
 */
public class BloodInfuserRecipeAppendix extends RecipeAppendix<RecipeBloodInfuser> {

    private static final int SLOT_OFFSET_X = 16;
    private static final int SLOT_OFFSET_Y = 23;
    private static final int START_X_RESULT = 68;

    private static final AdvancedButtonEnum INPUT = AdvancedButtonEnum.create();
    private static final AdvancedButtonEnum RESULT = AdvancedButtonEnum.create();
    private static final AdvancedButtonEnum PROMISE = AdvancedButtonEnum.create();

    public BloodInfuserRecipeAppendix(IInfoBook infoBook, RecipeBloodInfuser recipe) {
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
        return "tile.blocks.evilcraft.blood_infuser.name";
    }

    @Override
    public void bakeElement(InfoSection infoSection) {
        renderItemHolders.put(INPUT, new ItemButton(getInfoBook()));
        renderItemHolders.put(RESULT, new ItemButton(getInfoBook()));
        renderItemHolders.put(PROMISE, new ItemButton(getInfoBook()));
        super.bakeElement(infoSection);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void drawElementInner(ScreenInfoBook gui, int x, int y, int width, int height, int page, int mx, int my) {
        int middle = (width - SLOT_SIZE) / 2;
        gui.drawArrowRight(x + middle - 3, y + SLOT_OFFSET_Y + 2);

        // Prepare items
        int tick = getTick(gui);
        ItemStack input = prepareItemStacks(recipe.getInputIngredient().getMatchingStacks(), tick);
        ItemStack result = prepareItemStack(recipe.getOutputItem(), tick);
        ItemStack promise = null;
        if(recipe.getInputTier() > 0) {
            promise = new ItemStack(ItemPromise.getItem(recipe.getInputTier()));
        }

        // Items
        renderItem(gui, x + SLOT_OFFSET_X, y + SLOT_OFFSET_Y, input, mx, my, INPUT);
        renderItem(gui, x + START_X_RESULT, y + SLOT_OFFSET_Y, result, mx, my, RESULT);

        // Tier
        if(promise != null) {
            renderItem(gui, x + SLOT_OFFSET_X, y + 2, promise, mx, my, PROMISE);
        }

        renderItem(gui, x + middle, y + 2, ItemHelpers.getBloodBucket(), mx, my, false, null);
        renderItem(gui, x + middle, y + SLOT_OFFSET_Y, new ItemStack(RegistryEntries.BLOCK_BLOOD_INFUSER), mx, my, false, null);

        // Blood amount text
        FontRenderer fontRenderer = gui.getFontRenderer();
        boolean oldUnicode = fontRenderer.getBidiFlag();
        fontRenderer.setBidiFlag(true);
        fontRenderer.setBidiFlag(false);
        FluidStack fluidStack = recipe.getInputFluid();
        String line = fluidStack.getAmount() + " mB";
        fontRenderer.drawSplitString(line, x + middle + SLOT_SIZE + 1, y + 6, 200, 0);
        fontRenderer.setBidiFlag(oldUnicode);
    }
}
