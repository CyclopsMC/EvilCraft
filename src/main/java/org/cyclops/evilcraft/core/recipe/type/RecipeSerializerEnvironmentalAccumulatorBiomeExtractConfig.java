package org.cyclops.evilcraft.core.recipe.type;

import org.cyclops.cyclopscore.config.extendedconfig.RecipeConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the environmental accumulator biome extract recipe serializer.
 * @author rubensworks
 *
 */
public class RecipeSerializerEnvironmentalAccumulatorBiomeExtractConfig extends RecipeConfig<RecipeEnvironmentalAccumulatorBiomeExtract> {

    public RecipeSerializerEnvironmentalAccumulatorBiomeExtractConfig() {
        super(
                EvilCraft._instance,
                "environmental_accumulator_biome_extract",
                eConfig -> new RecipeSerializerEnvironmentalAccumulatorBiomeExtract()
        );
    }

}
