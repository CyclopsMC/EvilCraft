package org.cyclops.evilcraft.core.recipe.type;

import com.google.common.collect.Lists;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.Tags;
import org.cyclops.evilcraft.RegistryEntries;

import java.util.List;

/**
 * Recipe for crafting a dead bush using shears.
 * @author rubensworks
 *
 */
public class RecipeDeadBush extends CustomRecipe {

    private PlacementInfo placementInfo;

    public RecipeDeadBush() {
        super();
    }

    @Override
    public boolean isSpecial() {
        return false;
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
    public ItemStack assemble(CraftingInput inv) {
        return new ItemStack(Items.DEAD_BUSH);
    }

    public ItemStack getResultItem(HolderLookup.Provider registryAccess) {
        return new ItemStack(Items.DEAD_BUSH);
    }

    @Override
    public PlacementInfo placementInfo() {
        if (this.placementInfo == null) {
            this.placementInfo = PlacementInfo.create(Lists.newArrayList(Ingredient.of(BuiltInRegistries.ITEM.getOrThrow(ItemTags.SAPLINGS)), Ingredient.of(BuiltInRegistries.ITEM.getOrThrow(Tags.Items.TOOLS_SHEAR))));
        }
        return this.placementInfo;
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of(new ShapelessCraftingRecipeDisplay(
                List.of(new SlotDisplay.TagSlotDisplay(ItemTags.SAPLINGS), new SlotDisplay.TagSlotDisplay(Tags.Items.TOOLS_SHEAR)),
                new SlotDisplay.ItemSlotDisplay(Items.DEAD_BUSH),
                new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)
        ));
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput inv) {
        NonNullList<ItemStack> stacks = NonNullList.create();
        for (int i = 0; i < inv.size(); i++) {
            ItemStack itemStack = inv.getItem(i);
            if (itemStack.is(Tags.Items.TOOLS_SHEAR)) {
                itemStack = itemStack.copy();

                Player craftingPlayer = CommonHooks.getCraftingPlayer();
                if (craftingPlayer != null) {
                    // Regular item damaging if there is a player executing the recipe
                    itemStack.hurtAndBreak(1, craftingPlayer, EquipmentSlot.MAINHAND);
                } else {
                    // Fallback in case there is no crafting player
                    if (itemStack.getDamageValue() + 1 > itemStack.getMaxDamage()) {
                        itemStack.shrink(1);
                    } else {
                        itemStack.setDamageValue(itemStack.getDamageValue() + 1);
                    }
                }
            } else {
                net.minecraft.world.item.ItemStackTemplate remainder = itemStack.getCraftingRemainder();
                itemStack = remainder != null ? remainder.create() : ItemStack.EMPTY;
            }
            stacks.add(itemStack);
        }

        return stacks;
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return RegistryEntries.RECIPESERIALIZER_DEAD_BUSH.get();
    }
}
