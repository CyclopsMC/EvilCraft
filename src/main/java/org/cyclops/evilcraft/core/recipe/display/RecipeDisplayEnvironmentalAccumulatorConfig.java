package org.cyclops.evilcraft.core.recipe.display;

import org.cyclops.cyclopscore.config.extendedconfig.RecipeDisplayConfigCommon;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the drying basin recipe display.
 * @author rubensworks
 *
 */
public class RecipeDisplayEnvironmentalAccumulatorConfig extends RecipeDisplayConfigCommon<RecipeDisplayEnvironmentalAccumulator, EvilCraft> {

    public RecipeDisplayEnvironmentalAccumulatorConfig() {
        super(
                EvilCraft._instance,
                "environmental_accumulator",
                eConfig -> RecipeDisplayEnvironmentalAccumulator.TYPE
        );
    }
}
