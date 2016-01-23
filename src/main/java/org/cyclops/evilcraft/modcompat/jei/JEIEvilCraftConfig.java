package org.cyclops.evilcraft.modcompat.jei;

import mezz.jei.api.*;
import org.cyclops.evilcraft.client.gui.container.GuiBloodInfuser;
import org.cyclops.evilcraft.client.gui.container.GuiSanguinaryEnvironmentalAccumulator;
import org.cyclops.evilcraft.core.client.gui.container.GuiWorking;
import org.cyclops.evilcraft.modcompat.jei.bloodinfuser.BloodInfuserRecipeCategory;
import org.cyclops.evilcraft.modcompat.jei.bloodinfuser.BloodInfuserRecipeHandler;
import org.cyclops.evilcraft.modcompat.jei.bloodinfuser.BloodInfuserRecipeJEI;
import org.cyclops.evilcraft.modcompat.jei.environmentalaccumulator.EnvironmentalAccumulatorRecipeJEI;
import org.cyclops.evilcraft.modcompat.jei.sanguinaryenvironmentalaccumulator.SanguinaryEnvironmentalAccumulatorRecipeCategory;
import org.cyclops.evilcraft.modcompat.jei.sanguinaryenvironmentalaccumulator.SanguinaryEnvironmentalAccumulatorRecipeHandler;

/**
 * Helper for registering JEI manager.
 * @author rubensworks
 *
 */
@JEIPlugin
public class JEIEvilCraftConfig implements IModPlugin {

    public static IJeiHelpers JEI_HELPER;

    @Override
    public void onJeiHelpersAvailable(IJeiHelpers jeiHelpers) {
        JEI_HELPER = jeiHelpers;
    }

    @Override
    public void onItemRegistryAvailable(IItemRegistry itemRegistry) {

    }

    @Override
    public void register(IModRegistry registry) {
        if(JEIModCompat.canBeUsed) {
            // Blood Infuser
            registry.addRecipeCategories(new BloodInfuserRecipeCategory(JEI_HELPER.getGuiHelper()));
            registry.addRecipeHandlers(new BloodInfuserRecipeHandler());
            registry.addRecipes(BloodInfuserRecipeJEI.getAllRecipes());
            registry.addRecipeClickArea(GuiBloodInfuser.class,
                    GuiWorking.UPGRADES_OFFSET_X + GuiBloodInfuser.PROGRESSTARGETX, GuiBloodInfuser.PROGRESSTARGETY,
                    GuiBloodInfuser.PROGRESSWIDTH, GuiBloodInfuser.PROGRESSHEIGHT,
                    BloodInfuserRecipeHandler.CATEGORY);

            // Envir Acc
            // TODO
            registry.addRecipes(EnvironmentalAccumulatorRecipeJEI.getAllRecipes());

            // Sanguinary Envir Acc
            // TODO: recipe click buttons?
            registry.addRecipeCategories(new SanguinaryEnvironmentalAccumulatorRecipeCategory(JEI_HELPER.getGuiHelper()));
            registry.addRecipeHandlers(new SanguinaryEnvironmentalAccumulatorRecipeHandler());
            registry.addRecipeClickArea(GuiSanguinaryEnvironmentalAccumulator.class, GuiWorking.UPGRADES_OFFSET_X + GuiSanguinaryEnvironmentalAccumulator.PROGRESSTARGETX,
                    GuiSanguinaryEnvironmentalAccumulator.PROGRESSTARGETY, GuiSanguinaryEnvironmentalAccumulator.PROGRESSWIDTH,
                    GuiSanguinaryEnvironmentalAccumulator.PROGRESSHEIGHT, SanguinaryEnvironmentalAccumulatorRecipeHandler.CATEGORY);

            // Exalted Crafter
            // TODO

        }
    }

    @Override
    public void onRecipeRegistryAvailable(IRecipeRegistry recipeRegistry) {

    }
}
