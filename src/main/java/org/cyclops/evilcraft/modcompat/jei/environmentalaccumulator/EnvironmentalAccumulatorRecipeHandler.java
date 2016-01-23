package org.cyclops.evilcraft.modcompat.jei.environmentalaccumulator;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import org.cyclops.evilcraft.Reference;

import javax.annotation.Nonnull;

/**
 * Handler for the Envir Acc recipes.
 * @author rubensworks
 */
public class EnvironmentalAccumulatorRecipeHandler implements IRecipeHandler<EnvironmentalAccumulatorRecipeJEI> {

    public static final String CATEGORY = Reference.MOD_ID + ":environmentalAccumulator";

    @Nonnull
    @Override
    public Class<EnvironmentalAccumulatorRecipeJEI> getRecipeClass() {
        return EnvironmentalAccumulatorRecipeJEI.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return CATEGORY;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull EnvironmentalAccumulatorRecipeJEI recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull EnvironmentalAccumulatorRecipeJEI recipe) {
        return recipe != null;
    }

}
