package org.cyclops.evilcraft.infobook.pageelement;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.infobook.AdvancedButtonEnum;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.InfoSection;
import org.cyclops.cyclopscore.infobook.ScreenInfoBook;
import org.cyclops.cyclopscore.infobook.pageelement.RecipeAppendix;
import org.cyclops.evilcraft.api.broom.BroomModifier;
import org.cyclops.evilcraft.core.recipe.type.RecipeBloodInfuser;

import java.util.List;
import java.util.Map;

/**
 * Broom modifier info.
 * @author rubensworks
 */
public class BroomModifierRecipeAppendix extends RecipeAppendix<RecipeBloodInfuser> {

    private static final int START_X_RESULT = 68;

    private static final AdvancedButtonEnum INPUT = AdvancedButtonEnum.create();

    private final BroomModifier modifier;
    private final List<Pair<ItemStack, Float>> modifierValues;

    public BroomModifierRecipeAppendix(IInfoBook infoBook, BroomModifier modifier, Map<ItemStack, Float> modifierValues) {
        super(infoBook, null);
        this.modifier = modifier;
        this.modifierValues = Lists.newArrayList();
        for (Map.Entry<ItemStack, Float> entry : modifierValues.entrySet()) {
            this.modifierValues.add(Pair.of(entry.getKey(), entry.getValue()));
        }
    }

    @Override
    protected int getWidth() {
        return START_X_RESULT + 32;
    }

    @Override
    protected int getHeightInner() {
        return 15;
    }

    @Override
    protected String getUnlocalizedTitle() {
        return "broom.modifiers.evilcraft.type";
    }

    @Override
    public void bakeElement(InfoSection infoSection) {
        renderItemHolders.put(INPUT, new ItemButton(getInfoBook()));
        super.bakeElement(infoSection);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void drawElementInner(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, int width, int height, int page, int mx, int my) {
        int tick = getTick(gui);
        Pair<ItemStack, Float> value = modifierValues.get(tick % modifierValues.size());

        ItemStack input = value.getKey();

        // Items
        renderItem(gui, guiGraphics, x, y, input, mx, my, INPUT);

        // Effect
        String line = String.format("+ %s %s", value.getValue().toString(), L10NHelpers.localize(modifier.getTranslationKey()));
        drawString(gui, guiGraphics, line, x + SLOT_SIZE + 4, y + 3);
    }

    @OnlyIn(Dist.CLIENT)
    protected void drawString(ScreenInfoBook gui, GuiGraphics guiGraphics, String string, int x, int y) {
        Font fontRenderer = gui.getFont();
        MultiLineLabel.create(fontRenderer, Component.literal(string), 200)
                .renderLeftAlignedNoShadow(guiGraphics, x, y, 9, 0);
    }
}
