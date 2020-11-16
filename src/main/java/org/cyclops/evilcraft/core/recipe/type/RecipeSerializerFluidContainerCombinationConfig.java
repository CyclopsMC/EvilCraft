package org.cyclops.evilcraft.core.recipe.type;

import org.cyclops.cyclopscore.config.extendedconfig.RecipeConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link RecipeFluidContainerCombination}.
 * @author rubensworks
 */
public class RecipeSerializerFluidContainerCombinationConfig extends RecipeConfig<RecipeFluidContainerCombination> {

    public RecipeSerializerFluidContainerCombinationConfig() {
        super(EvilCraft._instance,
                "crafting_special_fluidcontainer_combination",
                eConfig -> new RecipeSerializerFluidContainerCombination());
    }

}
