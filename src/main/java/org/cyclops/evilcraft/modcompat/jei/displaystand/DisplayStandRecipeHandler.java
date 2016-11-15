package org.cyclops.evilcraft.modcompat.jei.displaystand;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import org.cyclops.evilcraft.core.recipe.DisplayStandRecipe;
import org.cyclops.evilcraft.modcompat.jei.JEIEvilCraftConfig;

import javax.annotation.Nonnull;

/**
 * Handler for the Blood Infuser recipes.
 * @author rubensworks
 */
public class DisplayStandRecipeHandler implements IRecipeHandler<DisplayStandRecipe> {

    @Nonnull
    @Override
    public Class<DisplayStandRecipe> getRecipeClass() {
        return DisplayStandRecipe.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return VanillaRecipeCategoryUid.CRAFTING;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull DisplayStandRecipe recipe) {
        return getRecipeCategoryUid();
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull DisplayStandRecipe recipe) {
        return new DisplayStandRecipeJEI(JEIEvilCraftConfig.JEI_HELPER, recipe);
    }

    @Override
    public boolean isRecipeValid(@Nonnull DisplayStandRecipe recipe) {
        return recipe.getInput() != null && recipe.getInput().length > 0;
    }

}
