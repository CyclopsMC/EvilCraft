package org.cyclops.evilcraft.core.recipe.type;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import org.cyclops.cyclopscore.recipe.ItemStackFromIngredient;
import org.cyclops.evilcraft.RegistryEntries;

import java.util.Optional;

/**
 * Blood Infuser recipe
 * @author rubensworks
 */
public class RecipeBloodInfuser implements Recipe<IInventoryFluidTier> {

    private final Optional<Ingredient> inputIngredient;
    private final Optional<FluidStack> inputFluid;
    private final Optional<Integer> inputTier;
    private final Either<ItemStack, ItemStackFromIngredient> outputItem;
    private final int duration;
    private final Optional<Float> xp;

    public RecipeBloodInfuser(Optional<Ingredient> inputIngredient, Optional<FluidStack> inputFluid, Optional<Integer> inputTier,
                              Either<ItemStack, ItemStackFromIngredient> outputItem, int duration, Optional<Float> xp) {
        this.inputIngredient = inputIngredient;
        this.inputFluid = inputFluid;
        this.inputTier = inputTier;
        this.outputItem = outputItem;
        this.xp = xp;
        this.duration = duration;
    }

    public Optional<Ingredient> getInputIngredient() {
        return inputIngredient;
    }

    public Optional<FluidStack> getInputFluid() {
        return inputFluid;
    }

    public Optional<Integer> getInputTier() {
        return inputTier;
    }

    public Either<ItemStack, ItemStackFromIngredient> getOutputItem() {
        return outputItem;
    }

    public ItemStack getOutputItemFirst() {
        return getOutputItem().map(l -> l, ItemStackFromIngredient::getFirstItemStack);
    }

    public int getDuration() {
        return duration;
    }

    public Optional<Float> getXp() {
        return xp;
    }

    @Override
    public boolean matches(IInventoryFluidTier inv, Level worldIn) {
        return this.getInputTier().map(t -> t <= inv.getTier()).orElse(true)
                && inputIngredient.map(p -> p.test(inv.getItem(0))).orElse(true)
                && inputFluid.map(f -> f.getFluid() == inv.getFluidHandler().getFluidInTank(0).getFluid()).orElse(true)
                && inputFluid.map(f -> f.getAmount() <= inv.getFluidHandler().getFluidInTank(0).getAmount()).orElse(true);
    }

    @Override
    public ItemStack assemble(IInventoryFluidTier inv, RegistryAccess registryAccess) {
        return this.getOutputItemFirst().copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height <= 1;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return getResultItem();
    }

    public ItemStack getResultItem() {
        return this.getOutputItemFirst().copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RegistryEntries.RECIPESERIALIZER_BLOOD_INFUSER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RegistryEntries.RECIPETYPE_BLOOD_INFUSER.get();
    }
}
