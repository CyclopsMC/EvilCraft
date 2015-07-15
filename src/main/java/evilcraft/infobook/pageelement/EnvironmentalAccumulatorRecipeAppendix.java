package evilcraft.infobook.pageelement;

import evilcraft.Reference;
import evilcraft.block.EnvironmentalAccumulator;
import evilcraft.client.gui.container.GuiOriginsOfDarkness;
import evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeComponent;
import evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeProperties;
import evilcraft.core.weather.WeatherType;
import evilcraft.infobook.AdvancedButton;
import evilcraft.infobook.InfoSection;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;

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

    public EnvironmentalAccumulatorRecipeAppendix(IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> recipe) {
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
        return "tile.blocks.environmentalAccumulator.name";
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
        ItemStack input = prepareItemStack(recipe.getInput().getItemStack(), tick);
        ItemStack result = prepareItemStack(recipe.getOutput().getItemStack(), tick);

        // Items
        renderItem(gui, x + SLOT_OFFSET_X, y + SLOT_OFFSET_Y, input, mx, my, INPUT);
        renderItem(gui, x + START_X_RESULT, y + SLOT_OFFSET_Y, result, mx, my, RESULT);

        renderItem(gui, x + middle, y + SLOT_OFFSET_Y, new ItemStack(EnvironmentalAccumulator.getInstance()), mx, my, false, null);

        // Draw weathers
        int inputX = X_ICON_OFFSETS.get(recipe.getInput().getWeatherType());
        Minecraft.getMinecraft().getTextureManager().bindTexture(WEATHERS);
        gui.drawTexturedModalRect(x + SLOT_OFFSET_X, y + Y_START, inputX, 0, 16, 16);
        gui.drawOuterBorder(x + SLOT_OFFSET_X, y + Y_START, SLOT_SIZE, SLOT_SIZE, 1, 1, 1, 0.2f);
        int outputX = X_ICON_OFFSETS.get(recipe.getOutput().getWeatherType());
        Minecraft.getMinecraft().getTextureManager().bindTexture(WEATHERS);
        gui.drawTexturedModalRect(x + START_X_RESULT, y + Y_START, outputX, 0, 16, 16);
        gui.drawOuterBorder(x + START_X_RESULT, y + Y_START, SLOT_SIZE, SLOT_SIZE, 1, 1, 1, 0.2f);
    }
}
