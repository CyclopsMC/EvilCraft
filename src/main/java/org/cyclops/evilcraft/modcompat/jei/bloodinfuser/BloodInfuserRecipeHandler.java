package org.cyclops.evilcraft.modcompat.jei.bloodinfuser;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

/**
 * @author rubensworks
 */
public class BloodInfuserRecipeHandler implements IRecipeHandler {
    @Nonnull
    @Override
    public Class getRecipeClass() {
        return null;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return null;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull Object recipe) {
        return null;
    }

    @Override
    public boolean isRecipeValid(@Nonnull Object recipe) {
        return false;
    }
}
