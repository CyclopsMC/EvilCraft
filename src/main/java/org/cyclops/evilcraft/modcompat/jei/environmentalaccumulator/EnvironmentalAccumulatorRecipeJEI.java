package org.cyclops.evilcraft.modcompat.jei.environmentalaccumulator;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import lombok.Data;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.evilcraft.block.EnvironmentalAccumulator;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeComponent;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeProperties;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Recipe wrapper for Envir Acc recipes
 * @author rubensworks
 */
@Data
public class EnvironmentalAccumulatorRecipeJEI extends EnvironmentalAccumulatorRecipeJEIBase {

    public EnvironmentalAccumulatorRecipeJEI(IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> recipe) {
        super(recipe);
    }

    public static List<EnvironmentalAccumulatorRecipeJEI> getAllRecipes() {
        return Lists.transform(EnvironmentalAccumulator.getInstance().getRecipeRegistry().allRecipes(), new Function<IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties>, EnvironmentalAccumulatorRecipeJEI>() {
            @Nullable
            @Override
            public EnvironmentalAccumulatorRecipeJEI apply(@Nullable IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> input) {
                return new EnvironmentalAccumulatorRecipeJEI(input);
            }
        });
    }
}
