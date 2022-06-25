package org.cyclops.evilcraft.core.recipe.type;

import org.cyclops.cyclopscore.config.extendedconfig.RecipeTypeConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the environmental accumulator recipe type.
 * @author rubensworks
 *
 */
public class RecipeTypeEnvironmentalAccumulatorConfig extends RecipeTypeConfig<RecipeEnvironmentalAccumulator> {
    public RecipeTypeEnvironmentalAccumulatorConfig() {
        super(
                EvilCraft._instance,
                "environmental_accumulator"
        );
    }
}
