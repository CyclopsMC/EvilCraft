package evilcraft.infobook.pageelement;

import evilcraft.api.recipes.custom.IRecipe;
import evilcraft.client.gui.container.GuiOriginsOfDarkness;
import evilcraft.core.recipe.custom.DurationRecipeProperties;
import evilcraft.core.recipe.custom.ItemFluidStackAndTierRecipeComponent;
import evilcraft.core.recipe.custom.ItemStackRecipeComponent;
import evilcraft.item.CreativeBloodDrop;
import evilcraft.item.Promise;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Blood Infuser recipes.
 * @author rubensworks
 */
public class BloodInfuserRecipeAppendix extends RecipeAppendix<IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationRecipeProperties>> {

    private static final int OFFSET_Y = 0;
    private static final int SLOT_OFFSET_X = 16;
    private static final int SLOT_OFFSET_Y = 23;
    private static final int START_X_RESULT = 68;

    public BloodInfuserRecipeAppendix(IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationRecipeProperties> recipe) {
        super(recipe);
    }

    @Override
    protected int getOffsetY() {
        return OFFSET_Y;
    }

    @Override
    protected int getWidth() {
        return START_X_RESULT + 32;
    }

    @Override
    protected int getHeight() {
        return 42;
    }

    @Override
    public void drawElement(GuiOriginsOfDarkness gui, int x, int y, int width, int height, int page, int mx, int my) {
        gui.drawOuterBorder(x - 1, y - 1, getWidth() + 2, getHeight() + 2, 0.5F, 0.5F, 0.5F, 0.4f);

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
            renderItem(gui, x + SLOT_OFFSET_X, y + 3, promise, mx, my);
        }

        // Blood
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderItem.getInstance().renderIcon(x + SLOT_OFFSET_X + 20, y + 3, CreativeBloodDrop.getInstance().getIconFromDamage(0), 16, 16);RenderHelper.disableStandardItemLighting();
        GL11.glPopMatrix();

        // Blood amount text
        FontRenderer fontRenderer = gui.getFontRenderer();
        boolean oldUnicode = fontRenderer.getUnicodeFlag();
        fontRenderer.setUnicodeFlag(true);
        fontRenderer.setBidiFlag(false);
        FluidStack fluidStack = recipe.getInput().getFluidStack();
        String line = fluidStack.amount + " mB";
        fontRenderer.drawSplitString(line, x + SLOT_OFFSET_X + 40, y + 6, 200, 0);
        fontRenderer.setUnicodeFlag(oldUnicode);

        // Tooltips
        renderItemTooltip(gui, x + SLOT_OFFSET_X, y + SLOT_OFFSET_Y, input, mx, my);
        renderItemTooltip(gui, x + START_X_RESULT, y + SLOT_OFFSET_Y, result, mx, my);
        if(promise != null) {
            renderItemTooltip(gui, x + SLOT_OFFSET_X, y, promise, mx, my);
        }
    }
}
