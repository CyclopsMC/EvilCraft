package org.cyclops.evilcraft.modcompat.jei.bloodinfuser;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.cyclopscore.recipe.custom.component.ItemStackRecipeComponent;
import org.cyclops.evilcraft.block.BloodInfuser;
import org.cyclops.evilcraft.core.recipe.custom.DurationXpRecipeProperties;
import org.cyclops.evilcraft.core.recipe.custom.ItemFluidStackAndTierRecipeComponent;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Recipe wrapper for Blood Infuser recipes
 * @author rubensworks
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class BloodInfuserRecipeJEI extends BlankRecipeWrapper {

    private final FluidStack fluidStack;
    private final int upgrade;
    private final List<ItemStack> input;
    private final List<ItemStack> output;

    public BloodInfuserRecipeJEI(IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationXpRecipeProperties> recipe) {
        this.fluidStack = recipe.getInput().getFluidStack();
        this.upgrade = recipe.getInput().getTier();
        this.input = recipe.getInput().getItemStacks();
        this.output = recipe.getOutput().getItemStacks();
    }

    @Override
    public List getInputs() {
        return input;
    }

    @Override
    public List getOutputs() {
        return output;
    }

    @Override
    public List<FluidStack> getFluidInputs() {
        return Lists.newArrayList(fluidStack);
    }

    public static List<BloodInfuserRecipeJEI> getAllRecipes() {
        return Lists.transform(BloodInfuser.getInstance().getRecipeRegistry().allRecipes(), new Function<IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationXpRecipeProperties>, BloodInfuserRecipeJEI>() {
            @Nullable
            @Override
            public BloodInfuserRecipeJEI apply(IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationXpRecipeProperties> input) {
                return new BloodInfuserRecipeJEI(input);
            }
        });
    }
}
