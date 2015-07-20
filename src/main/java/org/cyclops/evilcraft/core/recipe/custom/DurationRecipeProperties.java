package org.cyclops.evilcraft.core.recipe.custom;

import lombok.Data;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeProperties;

/**
 * Holds a duration (in ticks) for which a {@link org.cyclops.cyclopscore.recipe.custom.api.IRecipe} needs to be processed.
 * @author immortaleeb
 */
@Data
public class DurationRecipeProperties implements IRecipeProperties, IDurationRecipeProperties {
    private final int duration;
}
