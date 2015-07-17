package evilcraft.core.recipe.custom;

import evilcraft.api.recipes.custom.IRecipeProperties;
import lombok.Data;

/**
 * Holds a duration (in ticks) for which a {@link evilcraft.api.recipes.custom.IRecipe} needs to be processed and an amount of experience.
 * @author immortaleeb
 */
@Data
public class DurationXpRecipeProperties implements IRecipeProperties, IDurationRecipeProperties {
    private final int duration;
    private final float xp;
}
