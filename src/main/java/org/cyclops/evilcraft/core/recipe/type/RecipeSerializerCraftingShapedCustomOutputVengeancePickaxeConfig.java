package org.cyclops.evilcraft.core.recipe.type;

import org.cyclops.cyclopscore.config.extendedconfig.RecipeConfig;
import org.cyclops.cyclopscore.recipe.type.RecipeCraftingShapedCustomOutput;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for vengeance pickaxe recipes.
 * @author rubensworks
 */
public class RecipeSerializerCraftingShapedCustomOutputVengeancePickaxeConfig extends RecipeConfig<RecipeCraftingShapedCustomOutput> {

    public RecipeSerializerCraftingShapedCustomOutputVengeancePickaxeConfig() {
        super(EvilCraft._instance,
                "crafting_shaped_custom_output_vengeance_pickaxe",
                eConfig -> new RecipeCraftingShapedCustomOutput.Serializer(() -> RegistryEntries.ITEM_VENGEANCE_PICKAXE.get().getEnchantedItemStack()));
    }

}
