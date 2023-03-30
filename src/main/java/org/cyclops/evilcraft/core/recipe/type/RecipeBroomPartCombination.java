package org.cyclops.evilcraft.core.recipe.type;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.helper.InventoryHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.api.broom.BroomModifier;
import org.cyclops.evilcraft.api.broom.BroomModifiers;
import org.cyclops.evilcraft.api.broom.IBroom;
import org.cyclops.evilcraft.api.broom.IBroomPart;
import org.cyclops.evilcraft.core.broom.BroomParts;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Recipe for combining broom parts to build a broom.
 * @author rubensworks
 *
 */
public class RecipeBroomPartCombination extends CustomRecipe {

    public RecipeBroomPartCombination(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer grid, Level world) {
        return !assemble(grid, world.registryAccess()).isEmpty();
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return getResultItem();
    }

    public ItemStack getResultItem() {
        return new ItemStack(RegistryEntries.ITEM_BROOM);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inventory) {
        NonNullList<ItemStack> aitemstack = NonNullList.withSize(inventory.getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < aitemstack.size(); ++i) {
            ItemStack itemstack = inventory.getItem(i);
            aitemstack.set(i, net.minecraftforge.common.ForgeHooks.getCraftingRemainingItem(itemstack));
        }

        Pair<ItemStack, List<ItemStack>> result = getResult(inventory);
        if(result != null) {
            List<ItemStack> extraOutputs = result.getRight();
            for (ItemStack extraOutput : extraOutputs) {
                InventoryHelpers.tryReAddToStack(ForgeHooks.getCraftingPlayer(), ItemStack.EMPTY, extraOutput,
                        ForgeHooks.getCraftingPlayer().getUsedItemHand());
            }
        }

        return aitemstack;
    }

    protected Map<IBroomPart.BroomPartType, IBroomPart> indexifyParts(Collection<IBroomPart> parts) {
        Map<IBroomPart.BroomPartType, IBroomPart> map = Maps.newHashMap();
        for (IBroomPart part : parts) {
            if (part.getType() != IBroomPart.BroomPartType.MODIFIER) {
                map.put(part.getType(), part);
            }
        }

        return map;
    }

    protected Pair<ItemStack, List<ItemStack>> getResult(CraftingContainer grid) {
        ItemStack output = getResultItem().copy();
        List<ItemStack> extraOutputs = Lists.newLinkedList();

        int existingBroomSlot = -1;
        Map<IBroomPart.BroomPartType, IBroomPart> existingBroomParts = null;
        Map<IBroomPart.BroomPartType, IBroomPart> parts = Maps.newHashMap();
        List<Map<BroomModifier, Float>> rawModifiers = Lists.newLinkedList();

        // Loop over the grid and find an existing broom
        for (int j = 0; j < grid.getContainerSize(); j++) {
            ItemStack element = grid.getItem(j);
            if (!element.isEmpty() && element.getItem() instanceof IBroom) {
                Map<IBroomPart.BroomPartType, IBroomPart> currentExistingBroomParts = indexifyParts(BroomParts.REGISTRY.getBroomParts(element));
                if(currentExistingBroomParts != null && areValidBroomParts(currentExistingBroomParts.values()) && element.getCount() == 1) {
                    if (existingBroomParts == null) {
                        existingBroomParts = currentExistingBroomParts;
                        output = element.copy();
                        existingBroomSlot = j;
                    } else {
                        return null;
                    }
                }
            }
        }

        // Loop over the grid and find parts and modifiers
        for (int j = 0; j < grid.getContainerSize(); j++) {
            ItemStack element = grid.getItem(j);
            if (!element.isEmpty()) {
                IBroomPart part = BroomParts.REGISTRY.getPartFromItem(element);
                Map<BroomModifier, Float> modifier = BroomModifiers.REGISTRY.getModifiersFromItem(element);
                if (part != null) {
                    if (parts.containsKey(part.getType())) {
                        return null;
                    }
                    parts.put(part.getType(), part);
                } else if (modifier != null) {
                    rawModifiers.add(modifier);
                } else if (j != existingBroomSlot) {
                    return null;
                }
            }
        }

        // If we had a existing broom, check which parts are replaced
        if (existingBroomParts != null) {
            for (Map.Entry<IBroomPart.BroomPartType, IBroomPart> entry : existingBroomParts.entrySet()) {
                if(parts.containsKey(entry.getKey())) {
                    extraOutputs.add(Iterables.get(BroomParts.REGISTRY.getItemsFromPart(entry.getValue()), 0).copy());
                } else {
                    parts.put(entry.getKey(), entry.getValue());
                }
            }
        }

        // Validate parts
        if (!areValidBroomParts(parts.values())) {
            return null;
        }

        // Write broom parts
        BroomParts.REGISTRY.setBroomParts(output, parts.values());

        // Validate modifiers
        Map<BroomModifier, Float> broomModifiers = BroomModifiers.REGISTRY.getModifiers(output);
        Map<BroomModifier, Float> baseModifiers = BroomParts.REGISTRY.getBaseModifiersFromBroom(output);
        applyNewModifiers(broomModifiers, rawModifiers);
        if (!areValidBroomModifiers(broomModifiers, baseModifiers)) {
            return null;
        }

        // Write broom modifiers
        BroomModifiers.REGISTRY.setModifiers(output, broomModifiers);

        return Pair.of(output, extraOutputs);
    }

    private void applyNewModifiers(Map<BroomModifier, Float> baseModifiers, List<Map<BroomModifier, Float>> rawModifiers) {
        for (Map<BroomModifier, Float> modifierValue : rawModifiers) {
            for (Map.Entry<BroomModifier, Float> entry : modifierValue.entrySet()) {
                BroomModifier modifier = entry.getKey();
                Float value = baseModifiers.get(entry.getKey());
                if (value != null) {
                    baseModifiers.put(modifier, modifier.apply(value, Lists.newArrayList(entry.getValue())));
                } else {
                    baseModifiers.put(modifier, modifier.apply(modifier.getDefaultValue(), Lists.newArrayList(entry.getValue())));
                }
            }
        }
    }

    @Override
    public ItemStack assemble(CraftingContainer grid, RegistryAccess registryAccess) {
        Pair<ItemStack, List<ItemStack>> result = getResult(grid);
        if(result == null) {
            return ItemStack.EMPTY;
        }
        return result.getLeft();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 1;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RegistryEntries.RECIPESERIALIZER_BROOM_PART_COMBINATION;
    }

    private boolean areValidBroomParts(Collection<IBroomPart> parts) {
        Set<IBroomPart.BroomPartType> remainingRequiredTypes = Sets.newHashSet(IBroomPart.BroomPartType.BASE_TYPES);
        for (IBroomPart part : parts) {
            remainingRequiredTypes.remove(part.getType());
        }
        return remainingRequiredTypes.isEmpty();
    }

    private boolean areValidBroomModifiers(Map<BroomModifier, Float> broomModifiers, Map<BroomModifier, Float> baseModifiers) {
        int baseMaxModifiers = 0;
        if(baseModifiers.containsKey(BroomModifiers.MODIFIER_COUNT)) {
            baseMaxModifiers = (int) (float) baseModifiers.get(BroomModifiers.MODIFIER_COUNT);
        }
        int maxModifiers = baseMaxModifiers;
        int modifiers = 0;
        for (Map.Entry<BroomModifier, Float> entry : broomModifiers.entrySet()) {
            int tier = (int) Math.ceil(entry.getValue() / entry.getKey().getTierValue());
            if(tier > entry.getKey().getMaxTiers()) {
                return false;
            }
            if(entry.getKey() == BroomModifiers.MODIFIER_COUNT) {
                maxModifiers += (int) (float) entry.getValue();
            } else {
                modifiers += tier;
            }
        }
        broomModifiers.put(BroomModifiers.MODIFIER_COUNT, (float) maxModifiers - baseMaxModifiers);
        return modifiers <= maxModifiers;
    }
}
