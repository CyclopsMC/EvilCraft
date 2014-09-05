package evilcraft.core.recipes;

import lombok.Data;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class allows you to register new recipes for machines.
 * @see evilcraft.core.recipes.IRecipe
 * @see evilcraft.core.recipes.IRecipeInput
 * @see evilcraft.core.recipes.IRecipeOutput
 * @see evilcraft.core.recipes.IRecipeProperties
 * @see evilcraft.core.recipes.IMachine
 * @author immortaleeb
 */
public class SuperRecipeRegistry {
    private static final Map<IMachine, List<IRecipe>> recipes = new HashMap<IMachine, List<IRecipe>>();
    private static final Map<IMachine, RecipeRegistry> registries = new HashMap<IMachine, RecipeRegistry>();

    /**
     * Returns the registry that is responsible for all recipes of the given machine.
     * If no recipe exists when calling this function, it will explicitly be created.
     *
     * @param machine The machine for which we want the registry.
     * @param <M> The type of the machine.
     * @param <I> The type of input of all recipes for this machine.
     * @param <O> The type of ouput of all recipes for this machine.
     * @param <P> The type of properties of all recipes for this machine.
     * @return The registry responsible for all recipes of the given machine.
     */
    public static
        <M extends IMachine<M, I, O, P>, I extends IRecipeInput, O extends IRecipeOutput, P extends IRecipeProperties>
        RecipeRegistry<M, I, O, P> getRecipeRegistry(M machine) {

        RecipeRegistry<M, I, O, P> registry = registries.get(machine);

        if (registry == null) {
            registry = new RecipeRegistry<M, I, O, P>(machine);
            registries.put(machine, registry);
        }

        return registry;
    }

    private static List<IRecipe> getRecipes(IMachine machine) {
        List<IRecipe> list = recipes.get(machine);
        if (list == null) {
            list = new ArrayList<IRecipe>();
            recipes.put(machine, list);
        }
        return list;
    }

    /**
     * Finds all recipes who have the given named id.
     * @param namedId The unique named id of the recipe.
     * @return Returns the recipe whose named id matches the given named id.
     */
    public static RecipeMatch<IMachine, IRecipe> findRecipeByNamedId(String namedId) {
        return new RecipePropertyMatcher<IMachine, IRecipe, String>(namedId) {
            @Override
            public String getProperty(IMachine machine, IRecipe recipe) {
                return recipe.getNamedId();
            }
        }.findRecipe();
    }

    /**
     * Finds all recipes who have the given recipe input.
     * @param input The input of the recipe.
     * @return Returns a list of recipes whose input equal the recipe input that was given.
     */
    public static List<RecipeMatch<IMachine, IRecipe>> findRecipesByInput(IRecipeInput input) {
        return new RecipePropertyMatcher<IMachine, IRecipe, IRecipeInput>(input) {
            @Override
            public IRecipeInput getProperty(IMachine machine, IRecipe recipe) {
                return recipe.getInput();
            }
        }.findRecipes();
    }

    /**
     * Returns all the recipes for the given machine.
     * @param machine The machine for which the recipes should be returned.
     * @return A list of recipes that the given machine can process.
     */
    public static List<RecipeMatch<IMachine, IRecipe>> findRecipesByMachine(IMachine machine) {
        List<RecipeMatch<IMachine, IRecipe>> list = new ArrayList<RecipeMatch<IMachine, IRecipe>>();

        for (IRecipe recipe : getRecipes(machine)) {
            list.add(new RecipeMatch<IMachine, IRecipe>(machine, recipe));
        }

        return list;
    }

    /**
     * Returns the first recipe for which the matches() method of the given
     * {@link SuperRecipeRegistry.RecipeMatcher} returns true.
     * @param recipeMatcher A matcher that defines which recipes fit some matching criteria.
     * @return The first recipe which matches the criteria, or null in case no such recipe was found.
     */
    public static <M extends IMachine, R extends IRecipe> RecipeMatch<M, R> findRecipe(RecipeMatcher<M, R> recipeMatcher) {
        for (Map.Entry<IMachine, List<IRecipe>> entry : recipes.entrySet()) {
            IMachine machine = entry.getKey();
            List<IRecipe> recipes = entry.getValue();

            for (IRecipe recipe : recipes) {
                if (recipeMatcher.matches((M)machine, (R)recipe))
                    return new RecipeMatch<M, R>((M)machine, (R)recipe);
            }
        }

        return null;
    }

    /**
     * Returns a list of registered recipe for which the matches() method of the given
     * {@link SuperRecipeRegistry.RecipeMatcher} returns true.
     * @param recipeMatcher A matcher that defines which recipes fit some matching criteria.
     * @return A list of recipes that match the given criteria.
     */
    public static <M extends IMachine, R extends IRecipe> List<RecipeMatch<M, R>> findRecipes(RecipeMatcher<M, R> recipeMatcher) {
        List<RecipeMatch<M, R>> results = new ArrayList<RecipeMatch<M, R>>();

        for (Map.Entry<IMachine, List<IRecipe>> entry : recipes.entrySet()) {
            IMachine machine = entry.getKey();
            List<IRecipe> recipes = entry.getValue();

            for (IRecipe recipe : recipes) {
                if (recipeMatcher.matches((M)machine, (R)recipe))
                    results.add(new RecipeMatch<M, R>((M)machine, (R)recipe));
            }
        }

        return results;
    }

    /**
     * Helper class to abstract away some function logic (lambdas would make this way simpler though).
     * I allows you to define a matching criteria and allows you to filter out recipe that match these criteria
     * and those that do not. You do this by implementing the matches() method, which should return true when
     * the criteria of the given recipe are met, or false otherwise.
     */
    public static abstract class RecipeMatcher<M extends IMachine, R extends IRecipe> {
        public abstract boolean matches(M machine, R recipeToMatch);

        public RecipeMatch<M, R> findRecipe() {
            return SuperRecipeRegistry.findRecipe(this);
        }

        public List<RecipeMatch<M, R>> findRecipes() {
            return (SuperRecipeRegistry.findRecipes(this));
        }
    }

    /**
     * It allows you to match a property of a recipe to the properties of all other registered recipes and return
     * any recipes whose properties match. You do this by implementing the getProperty() method, which should return
     * the property (of type T) of the given recipe that should be matched with the property given to the constructor.
     * @param <P> The class of the property that should be matched.
     */
    public static abstract class RecipePropertyMatcher<M extends IMachine, R extends IRecipe, P> extends RecipeMatcher<M, R> {
        private final P property;

        public RecipePropertyMatcher(P property) {
            this.property = property;
        }

        @Override
        public boolean matches(M machine, R recipeToMatch) {
            P recipeProperty = getProperty(machine, recipeToMatch);
            return recipeProperty != null && recipeProperty.equals(property);
        }

        /**
         * Returns the property of the recipe that should be matched.
         * @param recipe A recipe whose properties should be matched.
         * @return The property of the given recipe that should be matched.
         */
        public abstract P getProperty(M machine, R recipe);
    }

    /**
     * A match for a recipe, holds the recipe and the machine it is associated with.
     * @param <M> The type of the machine.
     * @param <R> The type of the recipe.
     */
    @Data
    public static class RecipeMatch<M extends IMachine, R extends IRecipe> {
        private final M machine;
        private final R recipe;
    }

    /**
     * This class allows you to register and search for recipes that are associated with a specific machine.
     * In order to return an instance of this class use {@link evilcraft.core.recipes.SuperRecipeRegistry#getRecipes(IMachine)}
     * by passing the appropriate machine.
     * @param <M> The type of the machine.
     * @param <I> The type of the recipe input of all recipes associated with the machine.
     * @param <O> The type of the recipe output of all recipes associated with the machine.
     * @param <P> The type of the recipe properties of all recipes associated with the machine.
     */
    public static class RecipeRegistry<M extends IMachine<M, I, O, P>, I extends IRecipeInput, O extends IRecipeOutput, P extends IRecipeProperties> {
        private final M machine;

        private RecipeRegistry(M machine) {
            this.machine = machine;
        }

        /**
         * Registers the given recipe with this registry.
         * @param recipe A recipe that should be registered.
         */
        public void registerRecipe(IRecipe<I, O, P> recipe) {
            getRecipes(machine).add(recipe);
        }

        /**
         * Registers a new recipe with the given properties with this registry.
         * @param namedId A unique name for the given recipe.
         * @param input The input of the recipe.
         * @param output The output of the recipe.
         * @param properties Additional properties of the recipe.
         */
        public void registerRecipe(String namedId, I input, O output, P properties) {
            registerRecipe(new Recipe<I, O, P>(namedId, input, output, properties));
        }

        /**
         * Registers a new recipe with the given properties with this registry.
         * @param input The input of the recipe.
         * @param output The output of the recipe.
         * @param properties Additional properties of the recipe.
         */
        public void registerRecipe(I input, O output, P properties) {
            registerRecipe(new Recipe<I, O, P>(input, output, properties));
        }

        /**
         * Returns the first recipe whose named id matches the given named id.
         * @param namedId The unique named id of the recipe.
         * @return The first recipe whose named id matches, or null if no match was found.
         */
        public IRecipe<I, O, P> findRecipeByNamedId(String namedId) {
            return findRecipe(new RecipePropertyMatcher<M, IRecipe<I, O, P>, String>(namedId) {
                @Override
                public String getProperty(M machine, IRecipe<I, O, P> recipe) {
                    return recipe.getNamedId();
                }
            });
        }

        /**
         * Returns the first recipe whose input matches the given recipe input.
         * @param input The input of the recipe.
         * @return The first recipe whose input matches, or null if no match was found.
         */
        public IRecipe<I, O, P> findRecipeByInput(I input) {
            return findRecipe(new RecipePropertyMatcher<M, IRecipe<I, O, P>, I>(input) {

                @Override
                public I getProperty(M machine, IRecipe<I, O, P> recipe) {
                    return recipe.getInput();
                }
            });
        }

        /**
         * Returns a list of recipes whose input match the given recipe input.
         * @param input The input of the recipe.
         * @return A list of recipes whose recipe input match the given input.
         */
        public List<IRecipe<I, O, P>> findRecipesByInput(I input) {
            return findRecipes(new RecipePropertyMatcher<M, IRecipe<I, O, P>, I>(input) {

                @Override
                public I getProperty(M machine, IRecipe<I, O, P> recipe) {
                    return recipe.getInput();
                }
            });
        }

        /**
         * Returns the first recipe whose output matches the given recipe output.
         * @param output The output of the recipe.
         * @return The first recipe whose output matches, or null if no match was found.
         */
        public IRecipe<I, O, P> findRecipeByOutput(O output) {
            return findRecipe(new RecipePropertyMatcher<M, IRecipe<I, O, P>, O>(output) {

                @Override
                public O getProperty(M machine, IRecipe<I, O, P> recipe) {
                    return recipe.getOutput();
                }
            });
        }

        /**
         * Returns a list of recipes whose output match the given recipe output.
         * @param output The output of the recipe.
         * @return A list of recipes whose recipe output match the given output.
         */
        public List<IRecipe<I, O, P>> findRecipesByOutput(O output) {
            return findRecipes(new RecipePropertyMatcher<M, IRecipe<I, O, P>, O>(output) {

                @Override
                public O getProperty(M machine, IRecipe<I, O, P> recipe) {
                    return recipe.getOutput();
                }
            });
        }

        /**
         * Returns the first recipe for which the matches() method of the given RecipeMatcher returns true.
         * @param recipeMatcher Defines the criteria for finding recipes.
         * @return The first matched recipe, or null if no such recipe is found.
         */
        public IRecipe<I, O, P> findRecipe(RecipeMatcher<M, IRecipe<I, O, P>> recipeMatcher) {
            for (IRecipe r : getRecipes(machine)) {
                IRecipe<I, O, P> recipe = (IRecipe<I, O, P>)r;
                if (recipeMatcher.matches(machine, recipe))
                    return recipe;
            }

            return null;
        }

        /**
         * Returns a list of recipes for which the matches() method of the given RecipeMatcher return true.
         * @param recipeMatcher Defines the criteria for finding recipes.
         * @return A list of recipes that match the given criteria.
         */
        public List<IRecipe<I, O, P>> findRecipes(RecipeMatcher<M, IRecipe<I, O, P>> recipeMatcher) {
            List<IRecipe<I, O, P>> results = new ArrayList<IRecipe<I, O, P>>();

            for (IRecipe r : getRecipes(machine)) {
                IRecipe<I, O, P> recipe = (IRecipe<I, O, P>)r;
                if (recipeMatcher.matches(machine, recipe))
                    results.add(recipe);
            }

            return results;
        }

        /**
         * Returns a list of all recipes that are associated with the current machine.
         * @return A list of all recipes associated with the current machine.
         */
        public List<IRecipe<I, O, P>> allRecipes() {
            return findRecipes(new RecipeMatcher<M, IRecipe<I,O,P>>() {
                @Override
                public boolean matches(IMachine machine, IRecipe recipeToMatch) {
                    return true;
                }
            });
        }
    }
}
