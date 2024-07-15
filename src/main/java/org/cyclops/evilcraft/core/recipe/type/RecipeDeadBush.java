package org.cyclops.evilcraft.core.recipe.type;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.Tags;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Recipe for crafting a dead bush using shears.
 * @author rubensworks
 *
 */
public class RecipeDeadBush extends CustomRecipe {

    public RecipeDeadBush(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput inv, Level worldIn) {
        int bushes = 0;
        int shears = 0;
        for (int i = 0; i < inv.size(); i++) {
            ItemStack itemStack = inv.getItem(i);
            if (itemStack.is(ItemTags.SAPLINGS)) {
                bushes++;
            } else if (itemStack.is(Tags.Items.TOOLS_SHEAR)) {
                shears++;
            }
        }
        return bushes == 1 && shears == 1;
    }

    @Override
    public ItemStack assemble(CraftingInput inv, HolderLookup.Provider registryAccess) {
        return getResultItem(registryAccess).copy();
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registryAccess) {
        return new ItemStack(Items.DEAD_BUSH);
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.of(ItemTags.SAPLINGS), Ingredient.of(Tags.Items.TOOLS_SHEAR));
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height == 2;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput inv) {
        NonNullList<ItemStack> stacks = NonNullList.create();
        for (int i = 0; i < inv.size(); i++) {
            ItemStack itemStack = inv.getItem(i);
            if (itemStack.getItem() == Items.SHEARS) {
                itemStack = itemStack.copy();

                Player craftingPlayer = CommonHooks.getCraftingPlayer();
                if (craftingPlayer != null) {
                    // Regular item damaging if there is a player executing the recipe
                    itemStack.hurtAndBreak(1, craftingPlayer, EquipmentSlot.MAINHAND);
                } else {
                    // Fallback in case there is no crafting player
                    itemStack.setDamageValue(itemStack.getDamageValue() + 1);
                    if (itemStack.getDamageValue() > itemStack.getMaxDamage()) {
                        itemStack.shrink(1);
                    }
                }
            } else {
                itemStack = CommonHooks.getCraftingRemainingItem(itemStack);
            }
            stacks.add(itemStack);
        }

        return stacks;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RegistryEntries.RECIPESERIALIZER_DEAD_BUSH.get();
    }
}
