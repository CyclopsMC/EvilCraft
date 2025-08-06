package org.cyclops.evilcraft.infobook.pageelement;

import com.google.common.collect.Lists;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.infobook.AdvancedButtonEnum;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.InfoBookParser;
import org.cyclops.cyclopscore.infobook.InfoSection;
import org.cyclops.cyclopscore.infobook.pageelement.RecipeAppendix;
import org.cyclops.evilcraft.api.broom.BroomModifier;

import java.util.List;
import java.util.Map;

/**
 * Broom modifier info.
 * @author rubensworks
 */
public class BroomModifierRecipeAppendix extends RecipeAppendix<BroomModifierRecipeAppendixClient> {

    public static final int START_X_RESULT = 68;

    public static final AdvancedButtonEnum INPUT = AdvancedButtonEnum.create();

    protected final BroomModifier modifier;
    protected final List<Pair<ItemStack, Float>> modifierValues;

    public BroomModifierRecipeAppendix(IInfoBook infoBook, BroomModifier modifier, Map<ItemStack, Float> modifierValues) throws InfoBookParser.InvalidAppendixException {
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
    public BroomModifierRecipeAppendixClient constructSectionAppendixClient() {
        return new BroomModifierRecipeAppendixClient(this);
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
        getSectionAppendixClient().bakeElement(infoSection);
        super.bakeElement(infoSection);
    }
}
