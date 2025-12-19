package org.cyclops.evilcraft.infobook.pageelement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.TextAlignment;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import net.neoforged.neoforge.fluids.FluidStack;
import org.cyclops.cyclopscore.infobook.AdvancedButton;
import org.cyclops.cyclopscore.infobook.AdvancedButtonEnum;
import org.cyclops.cyclopscore.infobook.InfoSection;
import org.cyclops.cyclopscore.infobook.ScreenInfoBook;
import org.cyclops.cyclopscore.infobook.pageelement.RecipeAppendixClient;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.tickaction.sanguinaryenvironmentalaccumulator.AccumulateItemTickAction;
import org.cyclops.evilcraft.core.helper.ItemHelpers;
import org.cyclops.evilcraft.core.recipe.display.RecipeDisplayEnvironmentalAccumulator;
import org.cyclops.evilcraft.core.weather.WeatherType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author rubensworks
 */
public class EnvironmentalAccumulatorRecipeAppendixClient extends RecipeAppendixClient<EnvironmentalAccumulatorRecipeAppendix> {

    private static final Identifier WEATHERS = Identifier.fromNamespaceAndPath(Reference.MOD_ID, Reference.TEXTURE_PATH_GUI + "weathers.png");
    private static final Map<WeatherType, Integer> X_ICON_OFFSETS = new HashMap<WeatherType, Integer>();
    static {
        X_ICON_OFFSETS.put(WeatherType.CLEAR, 0);
        X_ICON_OFFSETS.put(WeatherType.RAIN, 16);
        X_ICON_OFFSETS.put(WeatherType.LIGHTNING, 32);
    }
    private static final int SLOT_OFFSET_X = 16;
    private static final int SLOT_OFFSET_Y = 23;
    private static final int Y_START = 2;
    protected static final int SLOT_SIZE = 16;

    protected EnvironmentalAccumulatorRecipeAppendixClient(EnvironmentalAccumulatorRecipeAppendix sectionAppendix) {
        super(sectionAppendix);
    }

    public void bakeElement(InfoSection infoSection) {
        Map<AdvancedButtonEnum, AdvancedButton> renderItemHolders = getSectionAppendix().getRenderItemHolders();
        renderItemHolders.put(EnvironmentalAccumulatorRecipeAppendix.INPUT, new ItemButton(getSectionAppendix().getInfoBook()));
        renderItemHolders.put(EnvironmentalAccumulatorRecipeAppendix.RESULT, new ItemButton(getSectionAppendix().getInfoBook()));
    }

    @Override
    public void drawElementInner(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, int width, int height, int page, int mx, int my) {
        boolean sanguinary = (getTick(gui) % 2) == 1;
        int middle = (width - SLOT_SIZE) / 2;
        gui.drawArrowRight(guiGraphics, x + middle - 3, y + SLOT_OFFSET_Y + 2);

        // Prepare items
        RecipeDisplayEntry recipeDisplay = getSectionAppendix().getRecipeDisplay();
        if (recipeDisplay == null) {
            return;
        }
        int tick = getTick(gui);
        ContextMap contextMap = SlotDisplayContext.fromLevel(Minecraft.getInstance().level);
        RecipeDisplayEnvironmentalAccumulator display = ((RecipeDisplayEnvironmentalAccumulator) recipeDisplay.display());
        ItemStack input = prepareItemStacks(display.inputIngredient().resolveForStacks(contextMap), tick);
        ItemStack result = prepareItemStack(display.outputItem().resolveForFirstStack(contextMap), tick);

        // Items
        renderItem(gui, guiGraphics, x + SLOT_OFFSET_X, y + SLOT_OFFSET_Y, input, mx, my, EnvironmentalAccumulatorRecipeAppendix.INPUT);
        renderItem(gui, guiGraphics, x + EnvironmentalAccumulatorRecipeAppendix.START_X_RESULT, y + SLOT_OFFSET_Y, result, mx, my, EnvironmentalAccumulatorRecipeAppendix.RESULT);

        renderItem(gui, guiGraphics, x + middle, y + SLOT_OFFSET_Y, new ItemStack(sanguinary
                ? RegistryEntries.BLOCK_SANGUINARY_ENVIRONMENTAL_ACCUMULATOR.get()
                : RegistryEntries.BLOCK_ENVIRONMENTAL_ACCUMULATOR.get()), mx, my, false, null);

        // Draw weathers
        Integer inputX = X_ICON_OFFSETS.get(display.inputWeather());
        if(inputX != null) {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, WEATHERS, x + SLOT_OFFSET_X, y + Y_START, inputX, 0, 16, 16, 2565, 256);
            gui.drawOuterBorder(guiGraphics, x + SLOT_OFFSET_X, y + Y_START, SLOT_SIZE, SLOT_SIZE, 1, 1, 1, 0.2f);
            Integer outputX = X_ICON_OFFSETS.get(display.outputWeather());
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, WEATHERS, x + EnvironmentalAccumulatorRecipeAppendix.START_X_RESULT, y + Y_START, outputX, 0, 16, 16, 256, 256);
            gui.drawOuterBorder(guiGraphics, x + EnvironmentalAccumulatorRecipeAppendix.START_X_RESULT, y + Y_START, SLOT_SIZE, SLOT_SIZE, 1, 1, 1, 0.2f);
        }
        if(sanguinary) {
            // Draw blood usage
            renderItem(gui, guiGraphics, x + middle, y + 2, ItemHelpers.getBloodBucket(), mx, my, false, null);

            // Blood amount text
            Font fontRenderer = gui.getFont();
            int amount = AccumulateItemTickAction.getUsage(display.cooldownTime());
            FluidStack fluidStack = new FluidStack(RegistryEntries.FLUID_BLOOD, amount);
            String line = fluidStack.getAmount() + " mB";
            MultiLineLabel.create(fontRenderer, Component.literal(line), 200)
                    .visitLines(TextAlignment.LEFT, x + middle - 5, y + SLOT_SIZE, 9, guiGraphics.textRenderer());
        }
    }

}
