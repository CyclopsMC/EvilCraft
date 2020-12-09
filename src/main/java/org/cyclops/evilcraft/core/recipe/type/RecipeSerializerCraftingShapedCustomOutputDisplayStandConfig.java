package org.cyclops.evilcraft.core.recipe.type;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import org.cyclops.cyclopscore.config.extendedconfig.RecipeConfig;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for display stands.
 * @author rubensworks
 */
public class RecipeSerializerCraftingShapedCustomOutputDisplayStandConfig extends RecipeConfig<RecipeCraftingShapedCustomOutput> {

    public RecipeSerializerCraftingShapedCustomOutputDisplayStandConfig() {
        super(EvilCraft._instance,
                "crafting_shaped_custom_output_display_stand",
                eConfig -> new RecipeSerializerCraftingShapedCustomOutput(() -> new ItemStack(RegistryEntries.BLOCK_DISPLAY_STAND), (inventory, staticOutput) -> {
                    ItemStack plankWoodStack = ItemStack.EMPTY;
                    for (int i = 0; i < inventory.getSizeInventory(); i++) {
                        for (Item plankType : ItemTags.PLANKS.getAllElements()) {
                            ItemStack itemStack = inventory.getStackInSlot(i);
                            if (!itemStack.isEmpty() && itemStack.getItem() == plankType) {
                                plankWoodStack = itemStack;
                            }
                        }
                    }
                    if (plankWoodStack.isEmpty()) {
                        return null;
                    }
                    BlockState plankWoodBlockState = BlockHelpers.getBlockStateFromItemStack(plankWoodStack);
                    return RegistryEntries.BLOCK_DISPLAY_STAND.getTypedDisplayStandItem(plankWoodBlockState);
                }));
    }

}
