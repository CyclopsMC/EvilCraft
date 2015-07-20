package org.cyclops.evilcraft.core.recipe.custom;

/**
 * Interface for RecipeProperties that hold a duration.
 */
public interface IDurationRecipeProperties {
    /**
     * @return Returns the duration (in ticks) needed to process the recipe associated with the current properties.
     */
    public int getDuration();
}
