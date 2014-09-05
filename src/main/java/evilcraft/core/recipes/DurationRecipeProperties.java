package evilcraft.core.recipes;

import lombok.Data;

/**
 * Holds a duration (in ticks) for which a {@link evilcraft.core.recipes.IRecipe} needs to be processed.
 * @author immortaleeb
 */
@Data
public class DurationRecipeProperties implements IRecipeProperties, IDurationRecipeProperties {
    private final int duration;
}
