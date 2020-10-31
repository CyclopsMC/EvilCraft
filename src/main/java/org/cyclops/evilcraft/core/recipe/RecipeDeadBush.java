package org.cyclops.evilcraft.core.recipe;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Recipe for crafting a dead bush using shears.
 * @author rubensworks
 *
 */
public class RecipeDeadBush extends SpecialRecipe {

	public RecipeDeadBush(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		boolean bush = false;
		boolean shears = true;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack itemStack = inv.getStackInSlot(i);
			if (ItemTags.SAPLINGS.contains(itemStack.getItem())) {
				bush = true;
			} else if (itemStack.getItem() == Items.SHEARS) {
				shears = true;
			}
		}
		return bush && shears;
	}

	@Override
	public ItemStack getCraftingResult(CraftingInventory inv) {
		return new ItemStack(Items.DEAD_BUSH);
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height == 2;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {
		NonNullList<ItemStack> stacks = NonNullList.create();
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack itemStack = inv.getStackInSlot(i);
			if (itemStack.getItem() == Items.SHEARS) {
				itemStack = itemStack.copy();

				PlayerEntity craftingPlayer = ForgeHooks.getCraftingPlayer();
				if (craftingPlayer != null) {
					// Regular item damaging if there is a player executing the recipe
					itemStack.damageItem(1, craftingPlayer, (p) ->{});
				} else {
					// Fallback in case there is no crafting player
					itemStack.setDamage(itemStack.getDamage() + 1);
					if (itemStack.getDamage() > itemStack.getMaxDamage()) {
						itemStack.shrink(1);
					}
				}
			} else {
				itemStack = ForgeHooks.getContainerItem(itemStack);
			}
			stacks.add(itemStack);
		}

		return stacks;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return RegistryEntries.RECIPESERIALIZER_DEAD_BUSH;
	}
}
