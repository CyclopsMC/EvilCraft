package evilcraft.core.recipes;

import evilcraft.api.recipes.custom.IRecipeProperties;
import lombok.Data;

/**
 * Holds a duration (in ticks) for which a {@link evilcraft.api.recipes.custom.IRecipe} needs to be processed.
 * @author immortaleeb
 */
@Data
public class DurationRecipeProperties implements IRecipeProperties, IDurationRecipeProperties {
    private final int duration;
}
