package org.cyclops.evilcraft.core.recipe.type;

import org.cyclops.cyclopscore.config.extendedconfig.RecipeTypeConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

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
        RegistryEntries.RECIPETYPE_ENVIRONMENTAL_ACCUMULATOR = getInstance();
    }

}
