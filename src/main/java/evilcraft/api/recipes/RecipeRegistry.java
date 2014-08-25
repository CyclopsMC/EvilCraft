package evilcraft.api.recipes;

import java.util.ArrayList;
import java.util.List;

/**
 * This class allows you to register new recipes for machines.
 * @see evilcraft.api.recipes.IRecipe
 * @see evilcraft.api.recipes.IRecipeInput
 * @see evilcraft.api.recipes.IRecipeOutput
 * @see evilcraft.api.recipes.IRecipeProperties
 * @see evilcraft.api.recipes.IMachine
 * @author immortaleeb
 */
public class RecipeRegistry {
    private static final List<IRecipe> recipes = new ArrayList<IRecipe>();

    /**
     * Registers the given recipe with this register.
     * @param recipe A recipe that should be registered.
     */
    public static void registerRecipe(IRecipe recipe) {
        recipes.add(recipe);
    }

    /**
     * Registers a new recipe with the given properties with this registry.
     * @param namedId A unique name for the given recipe.
     * @param machine The machine that can process this recipe.
     * @param input The input of the recipe.
     * @param output The output of the recipe.
     * @param properties Additional properties of the recipe.
     */
    public static void registerRecipe(String namedId, IMachine machine, IRecipeInput input, IRecipeOutput output, IRecipeProperties properties) {
        registerRecipe(new Recipe(namedId, machine, input, output, properties));
    }

    /**
     * Registers a new recipe with the given properties with this registry.
     * @param machine The machine that can process this recipe.
     * @param input The input of the recipe.
     * @param output The output of the recipe.
     * @param properties Additional properties of the recipe. 
     */
    public static void registerRecipe(IMachine machine, IRecipeInput input, IRecipeOutput output, IRecipeProperties properties) {
        registerRecipe(new Recipe(machine, input, output, properties));
    }

    /**
     * Finds a recipe with the given namedId.
     * @param namedId The unique name for the recipe that should be found.
     * @return Returns a recipe whose unique name matches namedId, or null in case no recipe was found.
     */
    public static IRecipe findRecipe(String namedId) {
        return new RecipeMatchCondition<String>() {
            @Override
            public String getProperty(IRecipe recipe) {
                return recipe.getNamedId();
            }
        }.getResult(namedId);
    }

    public static List<IRecipe> findRecipes(IRecipeInput input) {
        return new RecipeMatchCondition<IRecipeInput>() {
            @Override
            public IRecipeInput getProperty(IRecipe recipe) {
                return recipe.getInput();
            }
        }.getResults(input);
    }

    /**
     * Returns all the recipes for the given machine.
     * @param machine The machine for which the recipes should be returned.
     * @return A list of recipes that the given machine can process.
     */
    public static List<IRecipe> findRecipes(IMachine machine) {
        return new RecipeMatchCondition<IMachine>() {
            @Override
            public IMachine getProperty(IRecipe recipe) {
                return recipe.getMachine();
            }
        }.getResults(machine);
    }

    /**
     * Helper class to abstract away some function logic (lambdas would make this way simpler though).
     * It allows you to match a property of a recipe to the properties of all other registered recipes and return
     * any recipes whose properties match.
     * @param <T> The class of the property that should be matched.
     */
    public static abstract class RecipeMatchCondition<T> {
        /**
         * Returns the property of the recipe that should be matched.
         * @param recipe A recipe whose properties should be matched.
         * @return The property of the given recipe that should be matched.
         */
        public abstract T getProperty(IRecipe recipe);

        /**
         * Returns the first registered recipe whose property matches the given property.
         * @param property The property that should be matched.
         * @return Returns the first matched recipe, or null if no recipe was found.
         */
        public IRecipe getResult(T property) {
            for (IRecipe recipe : recipes) {
                T recipeProperty = getProperty(recipe);
                if (recipeProperty != null && recipeProperty.equals(property))
                    return recipe;
            }

            return null;
        }

        /**
         * Returns a list of registered recipes whose property matches the given property.
         * @param property The property that should be matched.
         * @return A list of registered recipes whose property matches.
         */
        public List<IRecipe> getResults(T property) {
            List<IRecipe> results = new ArrayList<IRecipe>();

            for (IRecipe recipe : recipes) {
                T recipeProperty = getProperty(recipe);
                if (recipeProperty != null && recipeProperty.equals(property))
                    results.add(recipe);
            }

            return results;
        }
    }
}
