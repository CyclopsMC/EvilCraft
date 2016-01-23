package org.cyclops.evilcraft.modcompat.jei.sanguinaryenvironmentalaccumulator;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.evilcraft.block.EnvironmentalAccumulator;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeComponent;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeProperties;
import org.cyclops.evilcraft.modcompat.jei.environmentalaccumulator.EnvironmentalAccumulatorRecipeJEI;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Recipe wrapper for Sanguinary Envir Acc recipes
 * @author rubensworks
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class SanguinaryEnvironmentalAccumulatorRecipeJEI extends EnvironmentalAccumulatorRecipeJEI {

    public SanguinaryEnvironmentalAccumulatorRecipeJEI(IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> recipe) {
        super(recipe);
    }

    public static List<SanguinaryEnvironmentalAccumulatorRecipeJEI> getAllSanguinaryRecipes() {
        return Lists.transform(EnvironmentalAccumulator.getInstance().getRecipeRegistry().allRecipes(), new Function<IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties>, SanguinaryEnvironmentalAccumulatorRecipeJEI>() {
            @Nullable
            @Override
            public SanguinaryEnvironmentalAccumulatorRecipeJEI apply(@Nullable IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> input) {
                return new SanguinaryEnvironmentalAccumulatorRecipeJEI(input);
            }
        });
    }
}
