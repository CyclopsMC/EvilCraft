package org.cyclops.evilcraft.modcompat.jei.bloodinfuser;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.evilcraft.block.BloodInfuser;
import org.cyclops.evilcraft.client.gui.container.GuiBloodInfuser;
import org.cyclops.evilcraft.item.Promise;
import org.cyclops.evilcraft.tileentity.TileBloodInfuser;
import org.cyclops.evilcraft.tileentity.TileWorking;

import javax.annotation.Nonnull;

/**
 * Category for the Blood Infuser recipes.
 * @author rubensworks
 */
public class BloodInfuserRecipeCategory implements IRecipeCategory {

    private static final int INPUT_SLOT = 0;
    private static final int UPGRADE_SLOT = 1;
    private static final int OUTPUT_SLOT = 2;
    private static final int FLUID_SLOT = 3;

    private final IDrawable background;
    private final IDrawableAnimated arrow;
    private final IDrawable tankOverlay;

    public BloodInfuserRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation resourceLocation = new ResourceLocation(org.cyclops.evilcraft.Reference.MOD_ID + ":" + BloodInfuser.getInstance().getGuiTexture("_jei"));
        this.background = guiHelper.createDrawable(resourceLocation, 0, 0, 130, 70);
        IDrawableStatic arrowDrawable = guiHelper.createDrawable(resourceLocation,
                146, 0, GuiBloodInfuser.PROGRESSWIDTH, GuiBloodInfuser.PROGRESSHEIGHT);
        this.arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);
        this.tankOverlay = guiHelper.createDrawable(resourceLocation, 130, 0, GuiBloodInfuser.TANKWIDTH, GuiBloodInfuser.TANKHEIGHT);
    }

    @Nonnull
    @Override
    public String getUid() {
        return BloodInfuserRecipeHandler.CATEGORY;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return L10NHelpers.localize(BloodInfuser.getInstance().getUnlocalizedName() + ".name");
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        arrow.draw(minecraft, 65, 28);
    }

    @Override
    public void drawAnimations(Minecraft minecraft) {

    }

    protected int getMaxTankSize(BloodInfuserRecipeJEI bloodInfuserRecipe) {
        return TileBloodInfuser.LIQUID_PER_SLOT * TileWorking.getTankTierMultiplier(bloodInfuserRecipe.getUpgrade());
    }

    protected ItemStack getPromise(BloodInfuserRecipeJEI bloodInfuserRecipe) {
        if(bloodInfuserRecipe.getUpgrade() == 0) {
            return null;
        }
        return new ItemStack(Promise.getInstance(), 1, bloodInfuserRecipe.getUpgrade() - 1);
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper) {
        recipeLayout.getItemStacks().init(INPUT_SLOT, true, 41, 27);
        recipeLayout.getItemStacks().init(UPGRADE_SLOT, false, 41, 7);
        recipeLayout.getItemStacks().init(OUTPUT_SLOT, false, 95, 27);

        if(recipeWrapper instanceof BloodInfuserRecipeJEI) {
            BloodInfuserRecipeJEI recipe = (BloodInfuserRecipeJEI) recipeWrapper;
            recipeLayout.getItemStacks().set(INPUT_SLOT, recipe.getInput());
            recipeLayout.getItemStacks().set(UPGRADE_SLOT, getPromise(recipe));
            recipeLayout.getItemStacks().set(OUTPUT_SLOT, recipe.getOutput());

            recipeLayout.getFluidStacks().init(FLUID_SLOT, true, 6, 6,
                    GuiBloodInfuser.TANKWIDTH, GuiBloodInfuser.TANKHEIGHT, getMaxTankSize(recipe), true, tankOverlay);
            recipeLayout.getFluidStacks().set(FLUID_SLOT, recipe.getFluidStack());
        }
    }
}
