package org.cyclops.evilcraft.core.recipe.type;

import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.config.extendedconfig.RecipeConfig;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for vengeance pickaxe recipes.
 * @author rubensworks
 */
public class RecipeSerializerCraftingShapedCustomOutputDarkTankLargeConfig extends RecipeConfig<RecipeCraftingShapedCustomOutput> {

    public RecipeSerializerCraftingShapedCustomOutputDarkTankLargeConfig() {
        super(EvilCraft._instance,
                "crafting_shaped_custom_output_dark_tank_large",
                eConfig -> new RecipeSerializerCraftingShapedCustomOutput(() -> {
                    ItemStack darkTankx9 = new ItemStack(RegistryEntries.ITEM_DARK_TANK);
                    FluidHelpers.getFluidHandlerItemCapacity(darkTankx9)
                            .ifPresent(fluidHandler -> fluidHandler.setCapacity(fluidHandler.getCapacity() * 9));
                    return darkTankx9;
                }));
    }

}
