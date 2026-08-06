package org.cyclops.evilcraft.core.recipe.type;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.cyclops.cyclopscore.RegistryEntries;

/**
 * Crafting recipe to clear the fluid content of a fluid container.
 * @author rubensworks
 */
public class RecipeFluidContainerClear extends CustomRecipe {

    private final Ingredient inputIngredient;

    public RecipeFluidContainerClear(Ingredient inputIngredient) {
        super();
        this.inputIngredient = inputIngredient;
    }

    public Ingredient getInputIngredient() {
        return inputIngredient;
    }

    @Override
    public boolean matches(CraftingInput grid, Level world) {
        return !assemble(grid).isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingInput grid) {
        ItemStack result = ItemStack.EMPTY;
        for (int i = 0; i < grid.size(); i++) {
            ItemStack element = grid.getItem(i);
            if (!element.isEmpty()) {
                if (!inputIngredient.test(element) || !result.isEmpty()) {
                    return ItemStack.EMPTY;
                }
                result = element.copyWithCount(1);
                result.remove(RegistryEntries.COMPONENT_FLUID_CONTENT);
            }
        }
        return result;
    }

    public ItemStack getResultItem() {
        return new ItemStack(inputIngredient.items().findFirst().get()); // This is just a dummy item!
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput grid) {
        return NonNullList.withSize(grid.size(), ItemStack.EMPTY);
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return org.cyclops.evilcraft.RegistryEntries.RECIPESERIALIZER_FLUIDCONTAINER_CLEAR.get();
    }
}
