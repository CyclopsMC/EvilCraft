package org.cyclops.evilcraft.core.recipe.type;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
                    for (int i = 0; i < inventory.getContainerSize(); i++) {
                        for (Item plankType : ItemTags.PLANKS.getValues()) {
                            ItemStack itemStack = inventory.getItem(i);
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
