package org.cyclops.evilcraft.core.recipe.type;

import org.cyclops.cyclopscore.config.extendedconfig.RecipeConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link RecipeFluidContainerCombination}.
 * @author rubensworks
 */
public class RecipeSerializerFluidContainerCombinationConfig extends RecipeConfigCommon<RecipeFluidContainerCombination, IModBase> {

    public RecipeSerializerFluidContainerCombinationConfig() {
        super(EvilCraft._instance,
                "crafting_special_fluidcontainer_combination",
                eConfig -> RecipeSerializerFluidContainerCombination.INSTANCE);
    }

}
