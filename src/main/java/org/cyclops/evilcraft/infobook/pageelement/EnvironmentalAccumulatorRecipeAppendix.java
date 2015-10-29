package org.cyclops.evilcraft.infobook.pageelement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.infobook.AdvancedButton;
import org.cyclops.cyclopscore.infobook.GuiInfoBook;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.InfoSection;
import org.cyclops.cyclopscore.infobook.pageelement.RecipeAppendix;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.block.EnvironmentalAccumulator;
import org.cyclops.evilcraft.block.SanguinaryEnvironmentalAccumulator;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeComponent;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeProperties;
import org.cyclops.evilcraft.core.weather.WeatherType;
import org.cyclops.evilcraft.item.BucketBloodConfig;
import org.cyclops.evilcraft.tileentity.TileSanguinaryEnvironmentalAccumulator;
import org.cyclops.evilcraft.tileentity.tickaction.sanguinaryenvironmentalaccumulator.AccumulateItemTickAction;

import java.util.HashMap;
import java.util.Map;

/**
 * Blood Infuser recipes.
 * @author rubensworks
 */
public class EnvironmentalAccumulatorRecipeAppendix extends RecipeAppendix<IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties>> {

    private static final ResourceLocation WEATHERS = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_GUI + "weathers.png");
    private static final Map<WeatherType, Integer> X_ICON_OFFSETS = new HashMap<WeatherType, Integer>();
    static {
        X_ICON_OFFSETS.put(WeatherType.CLEAR, 0);
        X_ICON_OFFSETS.put(WeatherType.RAIN, 16);
        X_ICON_OFFSETS.put(WeatherType.LIGHTNING, 32);
    }
    private static final int SLOT_OFFSET_X = 16;
    private static final int SLOT_OFFSET_Y = 23;
    private static final int START_X_RESULT = 68;
    private static final int Y_START = 2;

    private static final AdvancedButton.Enum INPUT = AdvancedButton.Enum.create();
    private static final AdvancedButton.Enum RESULT = AdvancedButton.Enum.create();

    public EnvironmentalAccumulatorRecipeAppendix(IInfoBook infoBook, IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> recipe) {
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
        return "tile.blocks.evilcraft.environmentalAccumulator.name";
    }

    @Override
    public void bakeElement(InfoSection infoSection) {
        renderItemHolders.put(INPUT, new ItemButton());
        renderItemHolders.put(RESULT, new ItemButton());
        super.bakeElement(infoSection);
    }

    @Override
    public void drawElementInner(GuiInfoBook gui, int x, int y, int width, int height, int page, int mx, int my) {
        boolean sanguinary = (getTick(gui) % 2) == 1;
        int middle = (width - SLOT_SIZE) / 2;
        gui.drawArrowRight(x + middle - 3, y + SLOT_OFFSET_Y + 2);

        // Prepare items
        int tick = getTick(gui);
        ItemStack input = prepareItemStacks(recipe.getInput().getItemStacks(), tick);
        ItemStack result = prepareItemStacks(recipe.getOutput().getItemStacks(), tick);

        // Items
        renderItem(gui, x + SLOT_OFFSET_X, y + SLOT_OFFSET_Y, input, mx, my, INPUT);
        renderItem(gui, x + START_X_RESULT, y + SLOT_OFFSET_Y, result, mx, my, RESULT);

        renderItem(gui, x + middle, y + SLOT_OFFSET_Y, new ItemStack(sanguinary
                ? SanguinaryEnvironmentalAccumulator.getInstance()
                : EnvironmentalAccumulator.getInstance()), mx, my, false, null);

        // Draw weathers
        Integer inputX = X_ICON_OFFSETS.get(recipe.getInput().getWeatherType());
        if(inputX != null) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(WEATHERS);
            gui.drawTexturedModalRect(x + SLOT_OFFSET_X, y + Y_START, inputX, 0, 16, 16);
            gui.drawOuterBorder(x + SLOT_OFFSET_X, y + Y_START, SLOT_SIZE, SLOT_SIZE, 1, 1, 1, 0.2f);
            Integer outputX = X_ICON_OFFSETS.get(recipe.getOutput().getWeatherType());
            Minecraft.getMinecraft().getTextureManager().bindTexture(WEATHERS);
            gui.drawTexturedModalRect(x + START_X_RESULT, y + Y_START, outputX, 0, 16, 16);
            gui.drawOuterBorder(x + START_X_RESULT, y + Y_START, SLOT_SIZE, SLOT_SIZE, 1, 1, 1, 0.2f);
        }
        if(sanguinary) {
            // Draw blood usage
            renderItem(gui, x + middle, y + 2, new ItemStack(BucketBloodConfig._instance.getItemInstance()), mx, my, false, null);

            // Blood amount text
            FontRenderer fontRenderer = gui.getFontRenderer();
            boolean oldUnicode = fontRenderer.getUnicodeFlag();
            fontRenderer.setUnicodeFlag(true);
            fontRenderer.setBidiFlag(false);
            int amount = AccumulateItemTickAction.getUsage(recipe.getProperties());
            FluidStack fluidStack = new FluidStack(TileSanguinaryEnvironmentalAccumulator.ACCEPTED_FLUID, amount);
            String line = fluidStack.amount + " mB";
            fontRenderer.drawSplitString(line, x + middle - 5, y + SLOT_SIZE, 200, 0);
            fontRenderer.setUnicodeFlag(oldUnicode);
        }
    }
}
