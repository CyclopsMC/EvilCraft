package org.cyclops.evilcraft.modcompat.jei.sanguinaryenvironmentalaccumulator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeComponent;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeProperties;
import org.cyclops.evilcraft.modcompat.jei.environmentalaccumulator.EnvironmentalAccumulatorRecipeJEIBase;

import java.util.List;

/**
 * Recipe wrapper for Sanguinary Envir Acc recipes
 * @author rubensworks
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class SanguinaryEnvironmentalAccumulatorRecipeJEI extends EnvironmentalAccumulatorRecipeJEIBase<SanguinaryEnvironmentalAccumulatorRecipeJEI> {

    public SanguinaryEnvironmentalAccumulatorRecipeJEI(IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> recipe) {
        super(recipe);
    }

    protected SanguinaryEnvironmentalAccumulatorRecipeJEI() {
        super();
    }

    @Override
    protected SanguinaryEnvironmentalAccumulatorRecipeJEI newInstance(IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> input) {
        return new SanguinaryEnvironmentalAccumulatorRecipeJEI(input);
    }

    public static List<SanguinaryEnvironmentalAccumulatorRecipeJEI> getAllSanguinaryRecipes() {
        return new SanguinaryEnvironmentalAccumulatorRecipeJEI().createAllRecipes();
    }
}
