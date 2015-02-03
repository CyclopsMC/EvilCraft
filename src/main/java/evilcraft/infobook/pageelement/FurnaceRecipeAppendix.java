package evilcraft.infobook.pageelement;

import evilcraft.client.gui.container.GuiOriginsOfDarkness;
import evilcraft.infobook.AdvancedButton;
import evilcraft.infobook.InfoSection;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.Map;

/**
 * Blood Infuser recipes.
 * @author rubensworks
 */
public class FurnaceRecipeAppendix extends RecipeAppendix<Map.Entry<ItemStack, ItemStack>> {

    private static final int SLOT_OFFSET_X = 16;
    private static final int SLOT_OFFSET_Y = 3;
    private static final int START_X_RESULT = 68;

    private static final AdvancedButton.Enum INPUT = AdvancedButton.Enum.create();
    private static final AdvancedButton.Enum RESULT = AdvancedButton.Enum.create();

    public FurnaceRecipeAppendix(Map.Entry<ItemStack, ItemStack> recipe) {
        super(recipe);
    }

    @Override
    protected int getWidth() {
        return START_X_RESULT + 32;
    }

    @Override
    protected int getHeightInner() {
        return 22;
    }

    @Override
    protected String getUnlocalizedTitle() {
        return "tile.furnace.name";
    }

    @Override
    public void bakeElement(InfoSection infoSection) {
        renderItemHolders.put(INPUT, new ItemButton());
        renderItemHolders.put(RESULT, new ItemButton());
        super.bakeElement(infoSection);
    }

    @Override
    public void drawElementInner(GuiOriginsOfDarkness gui, int x, int y, int width, int height, int page, int mx, int my) {
        int middle = (width - SLOT_SIZE) / 2;
        gui.drawArrowRight(x + middle - 3, y + SLOT_OFFSET_Y + 2);

        // Prepare items
        int tick = getTick(gui);
        ItemStack input = prepareItemStack(recipe.getKey(), tick);
        ItemStack result = prepareItemStack(recipe.getValue(), tick);

        // Items
        renderItem(gui, x + SLOT_OFFSET_X, y + SLOT_OFFSET_Y, input, mx, my, INPUT);
        renderItem(gui, x + START_X_RESULT, y + SLOT_OFFSET_Y, result, mx, my, RESULT);

        renderItem(gui, x + middle, y + SLOT_OFFSET_Y, new ItemStack(Blocks.furnace), mx, my, false, null);
    }
}
