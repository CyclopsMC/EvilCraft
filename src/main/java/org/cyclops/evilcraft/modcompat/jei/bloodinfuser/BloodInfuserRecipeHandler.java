package org.cyclops.evilcraft.modcompat.jei.bloodinfuser;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import org.cyclops.evilcraft.Reference;

import javax.annotation.Nonnull;

/**
 * Handler for the Blood Infuser recipes.
 * @author rubensworks
 */
public class BloodInfuserRecipeHandler implements IRecipeHandler<BloodInfuserRecipeJEI> {

    public static final String CATEGORY = Reference.MOD_ID + ":bloodInfuser";

    @Nonnull
    @Override
    public Class<BloodInfuserRecipeJEI> getRecipeClass() {
        return BloodInfuserRecipeJEI.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return CATEGORY;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull BloodInfuserRecipeJEI recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull BloodInfuserRecipeJEI recipe) {
        return recipe != null;
    }

}
