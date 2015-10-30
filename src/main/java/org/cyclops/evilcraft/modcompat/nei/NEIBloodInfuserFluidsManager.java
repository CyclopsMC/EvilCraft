package org.cyclops.evilcraft.modcompat.nei;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.cyclopscore.recipe.custom.component.ItemStackRecipeComponent;
import org.cyclops.evilcraft.block.BloodInfuser;
import org.cyclops.evilcraft.core.recipe.custom.DurationXpRecipeProperties;
import net.minecraft.item.ItemStack;
import org.cyclops.evilcraft.core.recipe.custom.ItemFluidStackAndTierRecipeComponent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Manager for fluid usages in the {@link BloodInfuser}.
 * @author rubensworks
 */
public class NEIBloodInfuserFluidsManager extends NEIBloodInfuserManager {

    public class CachedFluidRecipe extends TemplateRecipeHandler.CachedRecipe {
        public NEIBloodInfuserManager.FluidPair fuel;

        public CachedFluidRecipe(NEIBloodInfuserManager.FluidPair fuel) {
            this.fuel = fuel;
        }

        @Override
        public PositionedStack getIngredient() {
            return getRecipe(-1).getIngredient();
        }

        @Override
        public PositionedStack getResult() {
            return getRecipe(-1).getResult();
        }

        @Override
        public PositionedStack getOtherStack() {
            return fuel.stack;
        }
    }

    private ArrayList<CachedBloodInfuserRecipe> mbloodinfuser = new ArrayList<CachedBloodInfuserRecipe>();

    public NEIBloodInfuserFluidsManager() {
        super();
        loadAllBloodInfuserRecipes();
    }

    @Override
    protected CachedBloodInfuserRecipe getRecipe(int recipe) {
        return mbloodinfuser.get(cycleticks / 24 % mbloodinfuser.size());
    }

    @Override
    public String getRecipeName() {
        return BloodInfuser.getInstance().getLocalizedName() + " " + L10NHelpers.localize("gui.nei.fluids");
    }

    private void loadAllBloodInfuserRecipes() {
        List<CachedBloodInfuserRecipe> recipes = new LinkedList<CachedBloodInfuserRecipe>();

        for (IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationXpRecipeProperties> recipe :
                BloodInfuser.getInstance().getRecipeRegistry().allRecipes())
            mbloodinfuser.add(new CachedBloodInfuserRecipe(recipe));
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if(outputId.equals(getOverlayIdentifier()) && getClass() == NEIBloodInfuserFluidsManager.class)
            for(FluidPair fuel : afluids)
                arecipes.add(new CachedFluidRecipe(fuel));
    }

    public void loadUsageRecipes(ItemStack ingredient) {
        for(FluidPair fuel : afluids)
            if(fuel.stack.contains(ingredient))
                arecipes.add(new CachedFluidRecipe(fuel));
    }

    public String getOverlayIdentifier() {
        return getFluidOverlayIdentifier();
    }

}
