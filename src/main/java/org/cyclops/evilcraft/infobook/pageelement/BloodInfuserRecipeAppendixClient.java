package org.cyclops.evilcraft.infobook.pageelement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.TextAlignment;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.network.chat.Component;
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
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.core.helper.ItemHelpers;
import org.cyclops.evilcraft.core.recipe.display.RecipeDisplayBloodInfuser;
import org.cyclops.evilcraft.item.ItemPromise;

import java.util.Map;

/**
 * @author rubensworks
 */
public class BloodInfuserRecipeAppendixClient extends RecipeAppendixClient<BloodInfuserRecipeAppendix> {

    protected static final int SLOT_SIZE = 16;
    private static final int SLOT_OFFSET_X = 16;
    private static final int SLOT_OFFSET_Y = 23;

    protected BloodInfuserRecipeAppendixClient(BloodInfuserRecipeAppendix sectionAppendix) {
        super(sectionAppendix);
    }

    public void bakeElement(InfoSection infoSection) {
        Map<AdvancedButtonEnum, AdvancedButton> renderItemHolders = getSectionAppendix().getRenderItemHolders();
        renderItemHolders.put(BloodInfuserRecipeAppendix.INPUT, new ItemButton(getSectionAppendix().getInfoBook()));
        renderItemHolders.put(BloodInfuserRecipeAppendix.RESULT, new ItemButton(getSectionAppendix().getInfoBook()));
        renderItemHolders.put(BloodInfuserRecipeAppendix.PROMISE, new ItemButton(getSectionAppendix().getInfoBook()));
    }

    @Override
    public void drawElementInner(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, int width, int height, int page, int mx, int my) {
        int middle = (width - SLOT_SIZE) / 2;
        gui.drawArrowRight(guiGraphics, x + middle - 3, y + SLOT_OFFSET_Y + 2);

        // Prepare items
        RecipeDisplayEntry recipeDisplay = getSectionAppendix().getRecipeDisplay();
        if (recipeDisplay == null) {
            return;
        }
        int tick = getTick(gui);
        ContextMap contextMap = SlotDisplayContext.fromLevel(Minecraft.getInstance().level);
        RecipeDisplayBloodInfuser display = ((RecipeDisplayBloodInfuser) recipeDisplay.display());
        ItemStack input = prepareItemStacks(display.inputIngredient().resolveForStacks(contextMap), tick);
        ItemStack result = prepareItemStack(display.outputItem().resolveForFirstStack(contextMap), tick);
        int inputTier = display.inputTier();
        ItemStack promise = inputTier > 0 ? new ItemStack(ItemPromise.getItem(inputTier)) : null;

        // Items
        renderItem(gui, guiGraphics, x + SLOT_OFFSET_X, y + SLOT_OFFSET_Y, input, mx, my, BloodInfuserRecipeAppendix.INPUT);
        renderItem(gui, guiGraphics, x + BloodInfuserRecipeAppendix.START_X_RESULT, y + SLOT_OFFSET_Y, result, mx, my, BloodInfuserRecipeAppendix.RESULT);

        // Tier
        if(promise != null) {
            renderItem(gui, guiGraphics, x + SLOT_OFFSET_X, y + 2, promise, mx, my, BloodInfuserRecipeAppendix.PROMISE);
        }

        renderItem(gui, guiGraphics, x + middle, y + 2, ItemHelpers.getBloodBucket(), mx, my, false, null);
        renderItem(gui, guiGraphics, x + middle, y + SLOT_OFFSET_Y, new ItemStack(RegistryEntries.BLOCK_BLOOD_INFUSER.get()), mx, my, false, null);

        // Blood amount text
        Font fontRenderer = gui.getFont();
        FluidStack fluidStack = display.inputFluid();
        if (!fluidStack.isEmpty()) {
            String line = fluidStack.getAmount() + " mB";
            MultiLineLabel.create(fontRenderer, Component.literal(line), 200)
                    .visitLines(TextAlignment.LEFT, x + middle + SLOT_SIZE + 1, y + 6, 9, guiGraphics.textRenderer());
        }
    }

}
