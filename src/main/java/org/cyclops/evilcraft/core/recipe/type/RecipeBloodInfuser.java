package org.cyclops.evilcraft.core.recipe.type;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.recipe.ItemStackFromIngredient;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Blood Infuser recipe
 * @author rubensworks
 */
public class RecipeBloodInfuser implements Recipe<IInventoryFluidTier> {

    private final ResourceLocation id;
    private final Ingredient inputIngredient;
    private final FluidStack inputFluid;
    private final int inputTier;
    private final Either<ItemStack, ItemStackFromIngredient> outputItem;
    private final int duration;
    private final float xp;

    public RecipeBloodInfuser(ResourceLocation id,
                              Ingredient inputIngredient, FluidStack inputFluid, int inputTier,
                              Either<ItemStack, ItemStackFromIngredient> outputItem, int duration, float xp) {
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

    public Either<ItemStack, ItemStackFromIngredient> getOutputItem() {
        return outputItem;
    }

    public ItemStack getOutputItemFirst() {
        return getOutputItem().map(l -> l, ItemStackFromIngredient::getFirstItemStack);
    }

    public int getDuration() {
        return duration;
    }

    public float getXp() {
        return xp;
    }

    @Override
    public boolean matches(IInventoryFluidTier inv, Level worldIn) {
        return this.getInputTier() <= inv.getTier()
                && inputIngredient.test(inv.getItem(0))
                && inputFluid.getFluid() == inv.getFluidHandler().getFluidInTank(0).getFluid()
                && inputFluid.getAmount() <= inv.getFluidHandler().getFluidInTank(0).getAmount();
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
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RegistryEntries.RECIPESERIALIZER_BLOOD_INFUSER;
    }

    @Override
    public RecipeType<?> getType() {
        return RegistryEntries.RECIPETYPE_BLOOD_INFUSER;
    }
}
