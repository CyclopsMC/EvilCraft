package org.cyclops.evilcraft.core.recipe.type;

import org.cyclops.cyclopscore.config.extendedconfig.RecipeConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link RecipeFluidContainerClear}.
 * @author rubensworks
 */
public class RecipeSerializerFluidContainerClearConfig extends RecipeConfig<RecipeFluidContainerClear> {

    public RecipeSerializerFluidContainerClearConfig() {
        super(EvilCraft._instance,
                "crafting_special_fluidcontainer_clear",
                eConfig -> new RecipeSerializerFluidContainerClear());
    }
}
