package org.cyclops.evilcraft.core.recipe.type;

import net.minecraft.world.item.ItemStackTemplate;
import org.cyclops.cyclopscore.config.extendedconfig.RecipeConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.cyclopscore.recipe.type.RecipeCraftingShapedCustomOutput;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for box of eternal closure recipes.
 * @author rubensworks
 */
public class RecipeSerializerCraftingShapedCustomOutputBoxOfEternalClosureConfig extends RecipeConfigCommon<RecipeCraftingShapedCustomOutput, IModBase> {

    public RecipeSerializerCraftingShapedCustomOutputBoxOfEternalClosureConfig() {
        super(EvilCraft._instance,
                "crafting_shaped_custom_output_box_of_eternal_closure",
                eConfig -> new RecipeCraftingShapedCustomOutput.Serializer(() -> new ItemStackTemplate(RegistryEntries.BLOCK_BOX_OF_ETERNAL_CLOSURE.get().asItem())).getRecipeSerializer());
    }

}
