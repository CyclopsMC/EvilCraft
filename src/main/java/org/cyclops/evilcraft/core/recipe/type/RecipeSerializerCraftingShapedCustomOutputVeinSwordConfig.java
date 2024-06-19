package org.cyclops.evilcraft.core.recipe.type;

import org.cyclops.cyclopscore.config.extendedconfig.RecipeConfig;
import org.cyclops.cyclopscore.recipe.type.RecipeCraftingShapedCustomOutput;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for vein sword recipes.
 * @author rubensworks
 */
public class RecipeSerializerCraftingShapedCustomOutputVeinSwordConfig extends RecipeConfig<RecipeCraftingShapedCustomOutput> {

    public RecipeSerializerCraftingShapedCustomOutputVeinSwordConfig() {
        super(EvilCraft._instance,
                "crafting_shaped_custom_output_vein_sword",
                eConfig -> new RecipeCraftingShapedCustomOutput.Serializer(() -> RegistryEntries.ITEM_VEIN_SWORD.get().getEnchantedItemStack()));
    }

}
