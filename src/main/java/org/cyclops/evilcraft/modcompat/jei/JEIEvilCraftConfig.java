package org.cyclops.evilcraft.modcompat.jei;

import mezz.jei.api.*;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;
import org.cyclops.evilcraft.block.BloodStainedBlock;
import org.cyclops.evilcraft.block.InvisibleRedstoneBlock;
import org.cyclops.evilcraft.client.gui.container.GuiBloodInfuser;
import org.cyclops.evilcraft.client.gui.container.GuiExaltedCrafter;
import org.cyclops.evilcraft.client.gui.container.GuiSanguinaryEnvironmentalAccumulator;
import org.cyclops.evilcraft.core.client.gui.container.GuiWorking;
import org.cyclops.evilcraft.modcompat.jei.bloodinfuser.BloodInfuserRecipeCategory;
import org.cyclops.evilcraft.modcompat.jei.bloodinfuser.BloodInfuserRecipeHandler;
import org.cyclops.evilcraft.modcompat.jei.bloodinfuser.BloodInfuserRecipeJEI;
import org.cyclops.evilcraft.modcompat.jei.bloodinfuser.BloodInfuserRecipeTransferInfo;
import org.cyclops.evilcraft.modcompat.jei.environmentalaccumulator.EnvironmentalAccumulatorRecipeCategory;
import org.cyclops.evilcraft.modcompat.jei.environmentalaccumulator.EnvironmentalAccumulatorRecipeHandler;
import org.cyclops.evilcraft.modcompat.jei.environmentalaccumulator.EnvironmentalAccumulatorRecipeJEI;
import org.cyclops.evilcraft.modcompat.jei.exaltedcrafter.ExaltedCrafterRecipeTransferInfo;
import org.cyclops.evilcraft.modcompat.jei.sanguinaryenvironmentalaccumulator.SanguinaryEnvironmentalAccumulatorRecipeCategory;
import org.cyclops.evilcraft.modcompat.jei.sanguinaryenvironmentalaccumulator.SanguinaryEnvironmentalAccumulatorRecipeHandler;
import org.cyclops.evilcraft.modcompat.jei.sanguinaryenvironmentalaccumulator.SanguinaryEnvironmentalAccumulatorRecipeJEI;
import org.cyclops.evilcraft.modcompat.jei.sanguinaryenvironmentalaccumulator.SanguinaryEnvironmentalAccumulatorRecipeTransferInfo;

import javax.annotation.Nonnull;

/**
 * Helper for registering JEI manager.
 * @author rubensworks
 *
 */
@JEIPlugin
public class JEIEvilCraftConfig implements IModPlugin {

    public static IJeiHelpers JEI_HELPER;

    @Override
    public void register(@Nonnull IModRegistry registry) {
        JEI_HELPER = registry.getJeiHelpers();
        if(JEIModCompat.canBeUsed) {
            // Blood Infuser
            registry.addRecipes(BloodInfuserRecipeJEI.getAllRecipes());
            registry.addRecipeCategories(new BloodInfuserRecipeCategory(JEI_HELPER.getGuiHelper()));
            registry.addRecipeHandlers(new BloodInfuserRecipeHandler());
            registry.addRecipeClickArea(GuiBloodInfuser.class,
                    GuiWorking.UPGRADES_OFFSET_X + GuiBloodInfuser.PROGRESSTARGETX, GuiBloodInfuser.PROGRESSTARGETY,
                    GuiBloodInfuser.PROGRESSWIDTH, GuiBloodInfuser.PROGRESSHEIGHT,
                    BloodInfuserRecipeHandler.CATEGORY);
            registry.getRecipeTransferRegistry().addRecipeTransferHandler(new BloodInfuserRecipeTransferInfo());

            // Envir Acc
            registry.addRecipes(EnvironmentalAccumulatorRecipeJEI.getAllRecipes());
            registry.addRecipeCategories(new EnvironmentalAccumulatorRecipeCategory(JEI_HELPER.getGuiHelper()));
            registry.addRecipeHandlers(new EnvironmentalAccumulatorRecipeHandler());

            // Sanguinary Envir Acc
            registry.addRecipes(SanguinaryEnvironmentalAccumulatorRecipeJEI.getAllSanguinaryRecipes());
            registry.addRecipeCategories(new SanguinaryEnvironmentalAccumulatorRecipeCategory(JEI_HELPER.getGuiHelper()));
            registry.addRecipeHandlers(new SanguinaryEnvironmentalAccumulatorRecipeHandler());
            registry.addRecipeClickArea(GuiSanguinaryEnvironmentalAccumulator.class, GuiWorking.UPGRADES_OFFSET_X + GuiSanguinaryEnvironmentalAccumulator.PROGRESSTARGETX,
                    GuiSanguinaryEnvironmentalAccumulator.PROGRESSTARGETY, GuiSanguinaryEnvironmentalAccumulator.PROGRESSWIDTH,
                    GuiSanguinaryEnvironmentalAccumulator.PROGRESSHEIGHT, SanguinaryEnvironmentalAccumulatorRecipeHandler.CATEGORY);
            registry.getRecipeTransferRegistry().addRecipeTransferHandler(new SanguinaryEnvironmentalAccumulatorRecipeTransferInfo());

            // Exalted Crafter
            registry.addRecipeClickArea(GuiExaltedCrafter.class, 88, 32, 28, 23, VanillaRecipeCategoryUid.CRAFTING);
            registry.getRecipeTransferRegistry().addRecipeTransferHandler(new ExaltedCrafterRecipeTransferInfo());

            // Ignore items
            JEI_HELPER.getItemBlacklist().addItemToBlacklist(new ItemStack(BloodStainedBlock.getInstance()));
            JEI_HELPER.getItemBlacklist().addItemToBlacklist(new ItemStack(InvisibleRedstoneBlock.getInstance()));

        }
    }

    @Override
    public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime) {

    }
}
