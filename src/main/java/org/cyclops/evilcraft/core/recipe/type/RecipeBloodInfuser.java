package org.cyclops.evilcraft.core.recipe.type;

import com.mojang.datafixers.util.Either;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidStackTemplate;
import org.cyclops.cyclopscore.recipe.ItemStackFromIngredient;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.core.recipe.display.RecipeDisplayBloodInfuser;

import java.util.List;
import java.util.Optional;

/**
 * Blood Infuser recipe
 * @author rubensworks
 */
public class RecipeBloodInfuser implements Recipe<IInventoryFluidTier> {

    private final Optional<Ingredient> inputIngredient;
    private final Optional<FluidStackTemplate> inputFluid;
    private final Optional<Integer> inputTier;
    private final Either<ItemStackTemplate, ItemStackFromIngredient> outputItem;
    private final int duration;
    private final Optional<Float> xp;

    private PlacementInfo placementInfo;

    public RecipeBloodInfuser(Optional<Ingredient> inputIngredient, Optional<FluidStackTemplate> inputFluid, Optional<Integer> inputTier,
                              Either<ItemStackTemplate, ItemStackFromIngredient> outputItem, int duration, Optional<Float> xp) {
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

    public Optional<FluidStackTemplate> getInputFluid() {
        return inputFluid;
    }

    public Optional<Integer> getInputTier() {
        return inputTier;
    }

    public Either<ItemStackTemplate, ItemStackFromIngredient> getOutputItem() {
        return outputItem;
    }

    public ItemStack getOutputItemFirst() {
        return getOutputItem().map(ItemStackTemplate::create, ItemStackFromIngredient::getFirstItemStack);
    }

    public int getDuration() {
        return duration;
    }

    public Optional<Float> getXp() {
        return xp;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public boolean matches(IInventoryFluidTier inv, Level worldIn) {
        return this.getInputTier().map(t -> t <= inv.getTier()).orElse(true)
                && inputIngredient.map(p -> p.test(inv.getItem(0))).orElse(inv.getItem(0).isEmpty())
                && inputFluid.map(f -> f.fluid().value() == inv.getFluidHandler().getResource(0).getFluid()).orElse(inv.getFluidHandler().getResource(0).isEmpty())
                && inputFluid.map(f -> f.amount() <= inv.getFluidHandler().getAmountAsLong(0)).orElse(inv.getFluidHandler().getAmountAsLong(0) == 0);
    }

    @Override
    public ItemStack assemble(IInventoryFluidTier inv) {
        return this.getOutputItemFirst().copy();
    }

    public ItemStack getResultItem() {
        return this.getOutputItemFirst().copy();
    }

    @Override
    public RecipeSerializer<? extends Recipe<IInventoryFluidTier>> getSerializer() {
        return RegistryEntries.RECIPESERIALIZER_BLOOD_INFUSER.get();
    }

    @Override
    public RecipeType<? extends Recipe<IInventoryFluidTier>> getType() {
        return RegistryEntries.RECIPETYPE_BLOOD_INFUSER.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        if (this.placementInfo == null) {
            this.placementInfo = PlacementInfo.create(this.inputIngredient.orElse(Ingredient.of(Items.BUCKET)));
        }
        return this.placementInfo;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RegistryEntries.RECIPEBOOKCATEGORY_BLOOD_INFUSER.get();
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of(new RecipeDisplayBloodInfuser(
                this.getInputIngredient().map(Ingredient::display).orElse(SlotDisplay.Empty.INSTANCE),
                this.getInputFluid().map(FluidStackTemplate::create).orElse(FluidStack.EMPTY),
                this.getInputTier().orElse(-1),
                new SlotDisplay.ItemStackSlotDisplay(this.getOutputItem().map(l -> l, i -> ItemStackTemplate.fromNonEmptyStack(i.getFirstItemStack()))),
                new SlotDisplay.ItemSlotDisplay(RegistryEntries.BLOCK_BLOOD_INFUSER.get().asItem()),
                this.getDuration(),
                this.getXp().orElse(0F)
        ));
    }
}
