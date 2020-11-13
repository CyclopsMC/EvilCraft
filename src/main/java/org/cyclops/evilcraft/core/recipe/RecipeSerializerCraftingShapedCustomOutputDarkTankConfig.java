package org.cyclops.evilcraft.core.recipe;

import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.config.extendedconfig.RecipeConfig;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for vengeance pickaxe recipes.
 * @author rubensworks
 */
public class RecipeSerializerCraftingShapedCustomOutputDarkTankConfig extends RecipeConfig<RecipeCraftingShapedCustomOutput> {

    public RecipeSerializerCraftingShapedCustomOutputDarkTankConfig() {
        super(EvilCraft._instance,
                "crafting_shaped_custom_output_vengeance_pickaxe",
                eConfig -> new RecipeSerializerCraftingShapedCustomOutput(() -> {
                    ItemStack darkTankx9 = new ItemStack(RegistryEntries.ITEM_DARK_TANK);
                    FluidHelpers.getFluidHandlerItemCapacity(darkTankx9)
                            .ifPresent(fluidHandler -> fluidHandler.setCapacity(fluidHandler.getCapacity() * 9));
                    return darkTankx9;
                }));
    }

}
