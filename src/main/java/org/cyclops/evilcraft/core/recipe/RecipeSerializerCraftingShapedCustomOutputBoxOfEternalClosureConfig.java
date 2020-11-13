package org.cyclops.evilcraft.core.recipe;

import org.cyclops.cyclopscore.config.extendedconfig.RecipeConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockBoxOfEternalClosure;

/**
 * Config for box of eternal closure recipes.
 * @author rubensworks
 */
public class RecipeSerializerCraftingShapedCustomOutputBoxOfEternalClosureConfig extends RecipeConfig<RecipeCraftingShapedCustomOutput> {

    public RecipeSerializerCraftingShapedCustomOutputBoxOfEternalClosureConfig() {
        super(EvilCraft._instance,
                "crafting_shaped_custom_output_box_of_eternal_closure",
                eConfig -> new RecipeSerializerCraftingShapedCustomOutput(() -> BlockBoxOfEternalClosure.boxOfEternalClosureFilled));
    }

}
