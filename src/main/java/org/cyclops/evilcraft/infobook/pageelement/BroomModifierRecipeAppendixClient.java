package org.cyclops.evilcraft.infobook.pageelement;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.TextAlignment;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.infobook.InfoSection;
import org.cyclops.cyclopscore.infobook.ScreenInfoBook;
import org.cyclops.cyclopscore.infobook.pageelement.RecipeAppendixClient;

/**
 * @author rubensworks
 */
public class BroomModifierRecipeAppendixClient extends RecipeAppendixClient<BroomModifierRecipeAppendix> {

    protected static final int SLOT_SIZE = 16;

    protected BroomModifierRecipeAppendixClient(BroomModifierRecipeAppendix sectionAppendix) {
        super(sectionAppendix);
    }

    public void bakeElement(InfoSection infoSection) {
        getSectionAppendix().getRenderItemHolders().put(BroomModifierRecipeAppendix.INPUT, new ItemButton(getSectionAppendix().getInfoBook()));
    }

    @Override
    public void drawElementInner(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, int width, int height, int page, int mx, int my) {
        int tick = getTick(gui);
        Pair<ItemStack, Float> value = getSectionAppendix().modifierValues.get(tick % getSectionAppendix().modifierValues.size());

        ItemStack input = value.getKey();

        // Items
        renderItem(gui, guiGraphics, x, y, input, mx, my, BroomModifierRecipeAppendix.INPUT);

        // Effect
        String line = String.format("+ %s %s", value.getValue().toString(), IModHelpers.get().getL10NHelpers().localize(getSectionAppendix().modifier.getTranslationKey()));
        drawString(gui, guiGraphics, line, x + SLOT_SIZE + 4, y + 3);
    }

    protected void drawString(ScreenInfoBook gui, GuiGraphics guiGraphics, String string, int x, int y) {
        Font fontRenderer = gui.getFont();
        MultiLineLabel.create(fontRenderer, Component.literal(string), 200)
                .visitLines(TextAlignment.LEFT, x, y, 9, guiGraphics.textRenderer());
    }
}
