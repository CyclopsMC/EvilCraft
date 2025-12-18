package org.cyclops.evilcraft.core.recipe.type;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import org.cyclops.cyclopscore.config.extendedconfig.RecipeConfigCommon;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.cyclopscore.recipe.type.RecipeCraftingShapedCustomOutput;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for vengeance pickaxe recipes.
 * @author rubensworks
 */
public class RecipeSerializerCraftingShapedCustomOutputDarkTankLargeConfig extends RecipeConfigCommon<RecipeCraftingShapedCustomOutput, IModBase> {

    public RecipeSerializerCraftingShapedCustomOutputDarkTankLargeConfig() {
        super(EvilCraft._instance,
                "crafting_shaped_custom_output_dark_tank_large",
                eConfig -> new RecipeCraftingShapedCustomOutput.Serializer(() -> {
                    ItemAccess darkTankx9 = ItemAccess.forStack(new ItemStack(RegistryEntries.ITEM_DARK_TANK));
                    IModHelpersNeoForge.get().getFluidHelpers().getFluidHandlerItemCapacity(darkTankx9)
                            .ifPresent(fluidHandler -> fluidHandler.setTankCapacity(0, fluidHandler.getTankCapacity(0) * 9));
                    return darkTankx9.getResource().toStack(1);
                }));
    }

}
