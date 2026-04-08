package org.cyclops.evilcraft.core.recipe.type;

import org.cyclops.cyclopscore.config.extendedconfig.RecipeConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the environmental accumulator biome extract recipe serializer.
 * @author rubensworks
 *
 */
public class RecipeSerializerEnvironmentalAccumulatorBiomeExtractConfig extends RecipeConfigCommon<RecipeEnvironmentalAccumulatorBiomeExtract, IModBase> {

    public RecipeSerializerEnvironmentalAccumulatorBiomeExtractConfig() {
        super(
                EvilCraft._instance,
                "environmental_accumulator_biome_extract",
                eConfig -> new RecipeSerializerEnvironmentalAccumulatorBiomeExtract().createSerializer()
        );
    }

}
