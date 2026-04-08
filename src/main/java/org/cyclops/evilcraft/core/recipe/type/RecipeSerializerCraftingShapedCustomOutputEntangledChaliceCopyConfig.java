package org.cyclops.evilcraft.core.recipe.type;

import net.minecraft.world.item.ItemStackTemplate;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.cyclops.cyclopscore.config.extendedconfig.RecipeConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.cyclopscore.recipe.type.RecipeCraftingShapedCustomOutput;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.item.ItemEntangledChalice;

/**
 * Config for vengeance pickaxe recipes.
 * @author rubensworks
 */
public class RecipeSerializerCraftingShapedCustomOutputEntangledChaliceCopyConfig extends RecipeConfigCommon<RecipeCraftingShapedCustomOutput, IModBase> {

    public RecipeSerializerCraftingShapedCustomOutputEntangledChaliceCopyConfig() {
        super(EvilCraft._instance,
                "crafting_shaped_custom_output_entangled_chalice_copy",
                eConfig -> new RecipeCraftingShapedCustomOutput.Serializer(() -> new ItemStackTemplate(RegistryEntries.ITEM_ENTANGLED_CHALICE.get(), 2), (inventory, staticOutput) -> {
                    ItemAccess newStack = ItemAccess.forStack(staticOutput.copy());
                    String tankID = ((ItemEntangledChalice.FluidHandler) inventory.getItem(4).getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(inventory.getItem(4)))).getTankID();
                    try (var tx = Transaction.openRoot()) {
                        ((ItemEntangledChalice.FluidHandler) newStack.getCapability(Capabilities.Fluid.ITEM)).setTankID(tankID, tx);
                        tx.commit();
                    }
                    return newStack.getResource().toStack(2);
                }).getRecipeSerializer());
    }

}
