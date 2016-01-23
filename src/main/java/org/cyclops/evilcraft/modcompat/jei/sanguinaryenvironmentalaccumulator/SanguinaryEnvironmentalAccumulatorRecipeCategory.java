package org.cyclops.evilcraft.modcompat.jei.sanguinaryenvironmentalaccumulator;

import com.google.common.collect.Maps;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.block.SanguinaryEnvironmentalAccumulator;
import org.cyclops.evilcraft.client.gui.container.GuiBloodInfuser;
import org.cyclops.evilcraft.core.weather.WeatherType;
import org.cyclops.evilcraft.modcompat.jei.environmentalaccumulator.EnvironmentalAccumulatorRecipeJEI;
import org.cyclops.evilcraft.tileentity.TileSanguinaryEnvironmentalAccumulator;
import org.cyclops.evilcraft.tileentity.tickaction.sanguinaryenvironmentalaccumulator.AccumulateItemTickAction;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Category for the Blood Infuser recipes.
 * @author rubensworks
 */
public class SanguinaryEnvironmentalAccumulatorRecipeCategory implements IRecipeCategory {

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;
    private static final int FLUID_SLOT = 2;

    private final IDrawable background;
    private final IDrawableAnimated arrow;
    private final IDrawable tankOverlay;
    private final Map<WeatherType, IDrawableStatic> weatherIcons;

    private EnvironmentalAccumulatorRecipeJEI lastRecipe = null;

    public SanguinaryEnvironmentalAccumulatorRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation resourceLocation = new ResourceLocation(org.cyclops.evilcraft.Reference.MOD_ID + ":" + SanguinaryEnvironmentalAccumulator.getInstance().getGuiTexture("_jei"));
        this.background = guiHelper.createDrawable(resourceLocation, 0, 0, 130, 70);
        IDrawableStatic arrowDrawable = guiHelper.createDrawable(resourceLocation,
                146, 0, GuiBloodInfuser.PROGRESSWIDTH, GuiBloodInfuser.PROGRESSHEIGHT);
        this.arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);
        this.tankOverlay = guiHelper.createDrawable(resourceLocation, 130, 0, GuiBloodInfuser.TANKWIDTH, GuiBloodInfuser.TANKHEIGHT);
        weatherIcons = Maps.newHashMap();
        ResourceLocation weatherResourceLocation = new ResourceLocation(Reference.MOD_ID + ":" + Reference.TEXTURE_PATH_GUI + "weathers.png");
        weatherIcons.put(WeatherType.CLEAR, guiHelper.createDrawable(weatherResourceLocation, 0, 0, 16, 16));
        weatherIcons.put(WeatherType.RAIN, guiHelper.createDrawable(weatherResourceLocation, 16, 0, 16, 16));
        weatherIcons.put(WeatherType.LIGHTNING, guiHelper.createDrawable(weatherResourceLocation, 32, 0, 16, 16));
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
        arrow.draw(minecraft, 65, 28);
        if(lastRecipe != null) {
            if(lastRecipe.getInputWeather() != WeatherType.ANY) {
                weatherIcons.get(lastRecipe.getInputWeather()).draw(minecraft, 42, 8);
            }
            if(lastRecipe.getOutputWeather() != WeatherType.ANY) {
                weatherIcons.get(lastRecipe.getOutputWeather()).draw(minecraft, 96, 8);
            }
        }
    }

    @Override
    public void drawAnimations(Minecraft minecraft) {

    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper) {
        recipeLayout.getItemStacks().init(INPUT_SLOT, true, 41, 27);
        recipeLayout.getItemStacks().init(OUTPUT_SLOT, false, 95, 27);

        if(recipeWrapper instanceof EnvironmentalAccumulatorRecipeJEI) {
            EnvironmentalAccumulatorRecipeJEI recipe = (EnvironmentalAccumulatorRecipeJEI) recipeWrapper;
            this.lastRecipe = recipe;
            recipeLayout.getItemStacks().set(INPUT_SLOT, recipe.getInput());
            recipeLayout.getItemStacks().set(OUTPUT_SLOT, recipe.getOutput());

            FluidStack fluidStack = new FluidStack(TileSanguinaryEnvironmentalAccumulator.ACCEPTED_FLUID, AccumulateItemTickAction.getUsage(recipe.getProperties()));
            recipeLayout.getFluidStacks().init(FLUID_SLOT, true, 6, 6,
                    GuiBloodInfuser.TANKWIDTH, GuiBloodInfuser.TANKHEIGHT, fluidStack == null ? 0 : fluidStack.amount, true, tankOverlay);
            recipeLayout.getFluidStacks().set(FLUID_SLOT, fluidStack);
        } else {
            this.lastRecipe = null;
        }
    }
}
