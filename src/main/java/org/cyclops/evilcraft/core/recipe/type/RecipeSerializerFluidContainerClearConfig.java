package org.cyclops.evilcraft.core.recipe.type;

import org.cyclops.cyclopscore.config.extendedconfig.RecipeConfigCommon;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link RecipeFluidContainerClear}.
 * @author rubensworks
 */
public class RecipeSerializerFluidContainerClearConfig extends RecipeConfigCommon<RecipeFluidContainerClear, EvilCraft> {

    public RecipeSerializerFluidContainerClearConfig() {
        super(EvilCraft._instance,
                "crafting_special_fluidcontainer_clear",
                eConfig -> RecipeSerializerFluidContainerClear.SERIALIZER);
    }
}
