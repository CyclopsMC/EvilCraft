package org.cyclops.evilcraft.tileentity.environmentalaccumulator;

import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.evilcraft.block.EnvironmentalAccumulator;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeComponent;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeProperties;
import org.cyclops.evilcraft.tileentity.TileEnvironmentalAccumulator;

/**
 * This class represents an effect that is executed as soon
 * as an {@link EnvironmentalAccumulator} has finished processing
 * an item. Note that this effect is executed on the client
 * side only.
 * @author immortaleeb
 *
 */
public interface IEAProcessingFinishedEffect {
    /**
     * Function that needs to be executed when an item is done
     * processing. Note that this method is only executed
     * on the client side.
     * 
     * @param tile The tile entity that called this function.
     * @param recipe The recipe that caused this effect to be executed.
     */
    public void executeEffect(TileEnvironmentalAccumulator tile, IRecipe<EnvironmentalAccumulatorRecipeComponent,
                EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> recipe);
}
