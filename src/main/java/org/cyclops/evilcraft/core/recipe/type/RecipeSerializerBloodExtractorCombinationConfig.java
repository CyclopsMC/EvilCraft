package org.cyclops.evilcraft.core.recipe.type;

import org.cyclops.cyclopscore.config.extendedconfig.RecipeConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link RecipeBloodExtractorCombination}.
 * @author rubensworks
 */
public class RecipeSerializerBloodExtractorCombinationConfig extends RecipeConfig<RecipeBloodExtractorCombination> {

    public RecipeSerializerBloodExtractorCombinationConfig() {
        super(EvilCraft._instance,
                "crafting_special_bloodextractor_combination",
                eConfig -> new RecipeSerializerBloodExtractorCombination());
    }

}
