package org.cyclops.evilcraft.core.recipe.type;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Blood Infuser recipe
 * @author rubensworks
 */
public class RecipeBloodInfuser implements IRecipe<IInventoryFluidTier> {

    private final ResourceLocation id;
    private final Ingredient inputIngredient;
    private final FluidStack inputFluid;
    private final int inputTier;
    private final ItemStack outputItem;
    private final int duration;
    private final int xp;

    public RecipeBloodInfuser(ResourceLocation id,
                              Ingredient inputIngredient, FluidStack inputFluid, int inputTier,
                              ItemStack outputItem, int duration, int xp) {
        this.id = id;
        this.inputIngredient = inputIngredient;
        this.inputFluid = inputFluid;
        this.inputTier = inputTier;
        this.outputItem = outputItem;
        this.xp = xp;
        this.duration = duration;
    }

    public Ingredient getInputIngredient() {
        return inputIngredient;
    }

    public FluidStack getInputFluid() {
        return inputFluid;
    }

    public int getInputTier() {
        return inputTier;
    }

    public ItemStack getOutputItem() {
        return outputItem;
    }

    public int getDuration() {
        return duration;
    }

    public int getXp() {
        return xp;
    }

    @Override
    public boolean matches(IInventoryFluidTier inv, World worldIn) {
        return this.getInputTier() <= inv.getTier()
                && inputIngredient.test(inv.getStackInSlot(0))
                && inputFluid.getFluid() == inv.getFluidHandler().getFluidInTank(0).getFluid()
                && inputFluid.getAmount() <= inv.getFluidHandler().getFluidInTank(0).getAmount();
    }

    @Override
    public ItemStack getCraftingResult(IInventoryFluidTier inv) {
        return this.outputItem.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height <= 1;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.outputItem;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RegistryEntries.RECIPESERIALIZER_BLOOD_INFUSER;
    }

    @Override
    public IRecipeType<?> getType() {
        return RegistryEntries.RECIPETYPE_BLOOD_INFUSER;
    }
}
