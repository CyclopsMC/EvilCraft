package org.cyclops.evilcraft.core.recipe.type;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.cyclopscore.config.extendedconfig.RecipeConfig;
import org.cyclops.cyclopscore.helper.BlockHelpers;
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
                eConfig -> new RecipeSerializerCraftingShapedCustomOutput(() -> new ItemStack(RegistryEntries.BLOCK_DISPLAY_STAND), (inventory, staticOutput) -> {
                    Wrapper<ItemStack> plankWoodStack = new Wrapper<>(ItemStack.EMPTY);
                    for (int i = 0; i < inventory.getContainerSize(); i++) {
                        int finalI = i;
                        ForgeRegistries.ITEMS.tags().getTag(ItemTags.PLANKS).stream()
                                .forEach(plankType -> {
                                    ItemStack itemStack = inventory.getItem(finalI);
                                    if (!itemStack.isEmpty() && itemStack.getItem() == plankType) {
                                        plankWoodStack.set(itemStack);
                                    }
                                });
                    }
                    if (plankWoodStack.get().isEmpty()) {
                        return null;
                    }
                    BlockState plankWoodBlockState = BlockHelpers.getBlockStateFromItemStack(plankWoodStack.get());
                    return RegistryEntries.BLOCK_DISPLAY_STAND.getTypedDisplayStandItem(plankWoodBlockState);
                }));
    }

}
