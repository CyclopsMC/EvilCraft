package org.cyclops.evilcraft.core.recipe;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.cyclops.evilcraft.Reference;

import java.util.List;

/**
 * Recipe for crafting a dead bush using shears.
 * @author rubensworks
 *
 */
public class DeadBushRecipe extends ShapelessOreRecipe {

	public DeadBushRecipe() {
		super(new ResourceLocation(Reference.MOD_ID, "deadbush"),
				NonNullList.from(Ingredient.EMPTY, Ingredient.fromItem(Items.SHEARS), new OreIngredient("treeSapling")),
				new ItemStack(Blocks.DEADBUSH));
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		NonNullList<ItemStack> stacks = NonNullList.create();
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack itemStack = inv.getStackInSlot(i);
			if (itemStack.getItem() == Items.SHEARS) {
				itemStack = itemStack.copy();

				EntityPlayer craftingPlayer = ForgeHooks.getCraftingPlayer();
				if (craftingPlayer != null) {
					// Regular item damaging if there is a player executing the recipe
					itemStack.damageItem(1, craftingPlayer);
				} else {
					// Fallback in case there is no crafting player
					itemStack.setItemDamage(itemStack.getItemDamage() + 1);
					if (itemStack.getItemDamage() > itemStack.getMaxDamage()) {
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
}
