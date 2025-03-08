package org.cyclops.evilcraft.core.recipe.type;

import org.cyclops.cyclopscore.config.extendedconfig.RecipeTypeConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the environmental accumulator recipe type.
 * @author rubensworks
 *
 */
public class RecipeTypeEnvironmentalAccumulatorConfig extends RecipeTypeConfigCommon<RecipeEnvironmentalAccumulator, IModBase> {
    public RecipeTypeEnvironmentalAccumulatorConfig() {
        super(
                EvilCraft._instance,
                "environmental_accumulator"
        );
    }
}
