package org.cyclops.evilcraft.modcompat.minetweaker.handlers;

import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import org.cyclops.cyclopscore.modcompat.minetweaker.handlers.RecipeRegistryHandler;
import org.cyclops.cyclopscore.recipe.custom.Recipe;
import org.cyclops.cyclopscore.recipe.custom.component.ItemStackRecipeComponent;
import org.cyclops.evilcraft.block.BloodInfuser;
import org.cyclops.evilcraft.core.recipe.custom.DurationXpRecipeProperties;
import org.cyclops.evilcraft.core.recipe.custom.ItemFluidStackAndTierRecipeComponent;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.evilcraft.BloodInfuser")
public class BloodInfuserHandler extends RecipeRegistryHandler<BloodInfuser, ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationXpRecipeProperties> {

    private static final BloodInfuserHandler INSTANCE = new BloodInfuserHandler();

    @Override
    protected BloodInfuser getMachine() {
        return BloodInfuser.getInstance();
    }

    @Override
    protected String getRegistryName() {
        return "BloodInfuser";
    }

    @ZenMethod
    public static void addRecipe(IItemStack inputStack, ILiquidStack inputFluid, int tier,
                                 IItemStack outputStack, int duration, int xp) {
        INSTANCE.add(new Recipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationXpRecipeProperties>(
                new ItemFluidStackAndTierRecipeComponent(RecipeRegistryHandler.toStack(inputStack), RecipeRegistryHandler.toFluid(inputFluid), tier),
                new ItemStackRecipeComponent(RecipeRegistryHandler.toStack(outputStack)),
                new DurationXpRecipeProperties(duration, xp)));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack inputStack, ILiquidStack inputFluid, int tier,
                                    IItemStack outputStack, int duration, int xp) {
        INSTANCE.remove(new Recipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationXpRecipeProperties>(
                new ItemFluidStackAndTierRecipeComponent(RecipeRegistryHandler.toStack(inputStack), RecipeRegistryHandler.toFluid(inputFluid), tier),
                new ItemStackRecipeComponent(RecipeRegistryHandler.toStack(outputStack)),
                new DurationXpRecipeProperties(duration, xp)));
    }

    @ZenMethod
    public static void removeRecipesWithOutput(IItemStack outputStack) {
        INSTANCE.remove(
                new ItemStackRecipeComponent(RecipeRegistryHandler.toStack(outputStack))
        );
    }
}
