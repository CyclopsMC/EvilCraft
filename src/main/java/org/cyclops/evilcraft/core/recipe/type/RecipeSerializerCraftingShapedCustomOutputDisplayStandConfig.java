package org.cyclops.evilcraft.core.recipe.type;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.cyclops.cyclopscore.config.extendedconfig.RecipeConfig;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.recipe.type.RecipeCraftingShapedCustomOutput;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.core.algorithm.Wrapper;

/**
 * Config for display stands.
 * @author rubensworks
 */
public class RecipeSerializerCraftingShapedCustomOutputDisplayStandConfig extends RecipeConfig<RecipeCraftingShapedCustomOutput> {

    public RecipeSerializerCraftingShapedCustomOutputDisplayStandConfig() {
        super(EvilCraft._instance,
                "crafting_shaped_custom_output_display_stand",
                eConfig -> new RecipeCraftingShapedCustomOutput.Serializer(() -> new ItemStack(RegistryEntries.BLOCK_DISPLAY_STAND.get()), (inventory, staticOutput) -> {
                    Wrapper<ItemStack> plankWoodStack = new Wrapper<>(ItemStack.EMPTY);
                    for (int i = 0; i < inventory.size(); i++) {
                        int finalI = i;
                        BuiltInRegistries.ITEM.getTag(ItemTags.PLANKS).ifPresent(tag -> {
                            tag.stream().forEach(holder -> {
                                Item plankType = holder.value();
                                ItemStack itemStack = inventory.getItem(finalI);
                                if (!itemStack.isEmpty() && itemStack.getItem() == plankType) {
                                    plankWoodStack.set(itemStack);
                                }
                            });
                        });
                    }
                    if (plankWoodStack.get().isEmpty()) {
                        return null;
                    }
                    BlockState plankWoodBlockState = BlockHelpers.getBlockStateFromItemStack(plankWoodStack.get());
                    return RegistryEntries.BLOCK_DISPLAY_STAND.get().getTypedDisplayStandItem(plankWoodBlockState);
                }));
    }

}
