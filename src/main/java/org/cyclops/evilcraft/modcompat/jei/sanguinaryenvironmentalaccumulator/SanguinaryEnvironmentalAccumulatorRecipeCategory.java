package org.cyclops.evilcraft.modcompat.jei.sanguinaryenvironmentalaccumulator;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.evilcraft.block.SanguinaryEnvironmentalAccumulator;
import org.cyclops.evilcraft.client.gui.container.GuiBloodInfuser;
import org.cyclops.evilcraft.modcompat.jei.environmentalaccumulator.CommonEnvironmentalAccumulatorRecipeCategory;
import org.cyclops.evilcraft.modcompat.jei.environmentalaccumulator.EnvironmentalAccumulatorRecipeJEI;
import org.cyclops.evilcraft.tileentity.TileSanguinaryEnvironmentalAccumulator;
import org.cyclops.evilcraft.tileentity.tickaction.sanguinaryenvironmentalaccumulator.AccumulateItemTickAction;

import javax.annotation.Nonnull;

/**
 * Category for the Sanguinary Envir Acc recipes.
 * @author rubensworks
 */
public class SanguinaryEnvironmentalAccumulatorRecipeCategory extends CommonEnvironmentalAccumulatorRecipeCategory {

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;
    private static final int FLUID_SLOT = 2;

    private final IDrawable background;
    private final IDrawableAnimated arrow;
    private final IDrawable tankOverlay;

    public SanguinaryEnvironmentalAccumulatorRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, Pair.of(42, 8), Pair.of(96, 8));
        ResourceLocation resourceLocation = new ResourceLocation(org.cyclops.evilcraft.Reference.MOD_ID + ":" + SanguinaryEnvironmentalAccumulator.getInstance().getGuiTexture("_jei"));
        this.background = guiHelper.createDrawable(resourceLocation, 0, 0, 130, 70);
        IDrawableStatic arrowDrawable = guiHelper.createDrawable(resourceLocation,
                146, 0, GuiBloodInfuser.PROGRESSWIDTH, GuiBloodInfuser.PROGRESSHEIGHT);
        this.arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);
        this.tankOverlay = guiHelper.createDrawable(resourceLocation, 130, 0, GuiBloodInfuser.TANKWIDTH, GuiBloodInfuser.TANKHEIGHT);
    }

    @Nonnull
    @Override
    public String getUid() {
        return SanguinaryEnvironmentalAccumulatorRecipeHandler.CATEGORY;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return L10NHelpers.localize(SanguinaryEnvironmentalAccumulator.getInstance().getUnlocalizedName() + ".name");
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        super.drawExtras(minecraft);
        arrow.draw(minecraft, 65, 28);
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper) {
        super.setRecipe(recipeLayout, recipeWrapper);
        recipeLayout.getItemStacks().init(INPUT_SLOT, true, 41, 27);
        recipeLayout.getItemStacks().init(OUTPUT_SLOT, false, 95, 27);

        if(recipeWrapper instanceof EnvironmentalAccumulatorRecipeJEI) {
            EnvironmentalAccumulatorRecipeJEI recipe = (EnvironmentalAccumulatorRecipeJEI) recipeWrapper;
            recipeLayout.getItemStacks().set(INPUT_SLOT, recipe.getInput());
            recipeLayout.getItemStacks().set(OUTPUT_SLOT, recipe.getOutput());

            FluidStack fluidStack = new FluidStack(TileSanguinaryEnvironmentalAccumulator.ACCEPTED_FLUID, AccumulateItemTickAction.getUsage(recipe.getProperties()));
            recipeLayout.getFluidStacks().init(FLUID_SLOT, true, 6, 6,
                    GuiBloodInfuser.TANKWIDTH, GuiBloodInfuser.TANKHEIGHT, fluidStack == null ? 0 : fluidStack.amount, true, tankOverlay);
            recipeLayout.getFluidStacks().set(FLUID_SLOT, fluidStack);
        }
    }
}
