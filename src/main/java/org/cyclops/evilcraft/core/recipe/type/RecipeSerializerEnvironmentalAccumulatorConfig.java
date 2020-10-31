package org.cyclops.evilcraft.core.recipe.type;

import org.cyclops.cyclopscore.config.extendedconfig.RecipeConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the environmental accumulator recipe serializer.
 * @author rubensworks
 *
 */
public class RecipeSerializerEnvironmentalAccumulatorConfig extends RecipeConfig<RecipeEnvironmentalAccumulator> {

    public RecipeSerializerEnvironmentalAccumulatorConfig() {
        super(
                EvilCraft._instance,
                "environmental_accumulator",
                eConfig -> new RecipeSerializerEnvironmentalAccumulator()
        );
    }

}
