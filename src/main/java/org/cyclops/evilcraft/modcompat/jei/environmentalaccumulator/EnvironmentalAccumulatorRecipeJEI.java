package org.cyclops.evilcraft.modcompat.jei.environmentalaccumulator;

import lombok.Data;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeComponent;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeProperties;

import java.util.List;

/**
 * Recipe wrapper for Envir Acc recipes
 * @author rubensworks
 */
@Data
public class EnvironmentalAccumulatorRecipeJEI extends EnvironmentalAccumulatorRecipeJEIBase<EnvironmentalAccumulatorRecipeJEI> {

    public EnvironmentalAccumulatorRecipeJEI(IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> recipe) {
        super(recipe);
    }

    protected EnvironmentalAccumulatorRecipeJEI() {
        super();
    }

    @Override
    protected EnvironmentalAccumulatorRecipeJEI newInstance(IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> input) {
        return new EnvironmentalAccumulatorRecipeJEI(input);
    }

    public static List<EnvironmentalAccumulatorRecipeJEI> getAllRecipes() {
        return new EnvironmentalAccumulatorRecipeJEI().createAllRecipes();
    }
}
