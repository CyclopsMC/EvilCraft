package org.cyclops.evilcraft.infobook.pageelement;

import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import org.cyclops.cyclopscore.infobook.AdvancedButtonEnum;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.InfoBookParser;
import org.cyclops.cyclopscore.infobook.InfoSection;
import org.cyclops.cyclopscore.infobook.pageelement.RecipeAppendix;

import java.util.function.Supplier;

/**
 * Blood Infuser recipes.
 * @author rubensworks
 */
public class EnvironmentalAccumulatorRecipeAppendix extends RecipeAppendix<EnvironmentalAccumulatorRecipeAppendixClient> {

    public static final int START_X_RESULT = 68;

    public static final AdvancedButtonEnum INPUT = AdvancedButtonEnum.create();
    public static final AdvancedButtonEnum RESULT = AdvancedButtonEnum.create();

    public EnvironmentalAccumulatorRecipeAppendix(IInfoBook infoBook, Supplier<RecipeDisplayEntry> recipeDisplaySupplier) throws InfoBookParser.InvalidAppendixException {
        super(infoBook, recipeDisplaySupplier);
    }

    @Override
    protected int getWidth() {
        return START_X_RESULT + 32;
    }

    @Override
    public EnvironmentalAccumulatorRecipeAppendixClient constructSectionAppendixClient() {
        return new EnvironmentalAccumulatorRecipeAppendixClient(this);
    }

    @Override
    protected int getHeightInner() {
        return 42;
    }

    @Override
    protected String getUnlocalizedTitle() {
        return "block.evilcraft.environmental_accumulator";
    }

    @Override
    public void bakeElement(InfoSection infoSection) {
        getSectionAppendixClient().bakeElement(infoSection);
        super.bakeElement(infoSection);
    }
}
