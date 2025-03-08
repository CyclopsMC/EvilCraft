package org.cyclops.evilcraft.core.recipe.type;

import org.cyclops.cyclopscore.config.extendedconfig.RecipeConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link RecipeBloodExtractorCombination}.
 * @author rubensworks
 */
public class RecipeSerializerBloodExtractorCombinationConfig extends RecipeConfigCommon<RecipeBloodExtractorCombination, IModBase> {

    public RecipeSerializerBloodExtractorCombinationConfig() {
        super(EvilCraft._instance,
                "crafting_special_bloodextractor_combination",
                eConfig -> new RecipeSerializerBloodExtractorCombination());
    }

}
