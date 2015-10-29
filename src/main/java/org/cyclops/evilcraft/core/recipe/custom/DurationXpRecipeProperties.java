package org.cyclops.evilcraft.core.recipe.custom;

import lombok.Data;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeProperties;
import org.cyclops.evilcraft.core.recipe.custom.IDurationRecipeProperties;

/**
 * Holds a duration (in ticks) for which a {@link org.cyclops.cyclopscore.recipe.custom.api.IRecipe} needs to be processed and an amount of experience.
 * @author immortaleeb
 */
@Data
public class DurationXpRecipeProperties implements IRecipeProperties, IDurationRecipeProperties {
    private final int duration;
    private final float xp;
}
