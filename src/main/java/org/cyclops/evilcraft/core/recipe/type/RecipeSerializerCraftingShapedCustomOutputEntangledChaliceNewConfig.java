package org.cyclops.evilcraft.core.recipe.type;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidUtil;
import org.cyclops.cyclopscore.config.extendedconfig.RecipeConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.item.ItemEntangledChalice;

/**
 * Config for vengeance pickaxe recipes.
 * @author rubensworks
 */
public class RecipeSerializerCraftingShapedCustomOutputEntangledChaliceNewConfig extends RecipeConfig<RecipeCraftingShapedCustomOutput> {

    public RecipeSerializerCraftingShapedCustomOutputEntangledChaliceNewConfig() {
        super(EvilCraft._instance,
                "crafting_shaped_custom_output_entangled_chalice_new",
                eConfig -> new RecipeSerializerCraftingShapedCustomOutput(() -> new ItemStack(RegistryEntries.ITEM_ENTANGLED_CHALICE, 2), (inventory, staticOutput) -> {
                    ItemStack newStack = new ItemStack(RegistryEntries.ITEM_ENTANGLED_CHALICE);
                    ItemEntangledChalice.FluidHandler fluidHandler = (ItemEntangledChalice.FluidHandler) FluidUtil.getFluidHandler(newStack).orElse(null);
                    if (!MinecraftHelpers.isClientSideThread()) {
                        fluidHandler.setNextTankID();
                    }
                    ItemStack output = fluidHandler.getContainer();
                    output.setCount(2);
                    return output;
                }));
    }

}
