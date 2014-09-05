package evilcraft.core.recipe.custom;

import java.util.ArrayList;
import java.util.List;

import evilcraft.api.RegistryManager;
import evilcraft.api.recipes.custom.IMachine;
import evilcraft.api.recipes.custom.IRecipe;
import evilcraft.api.recipes.custom.IRecipeInput;
import evilcraft.api.recipes.custom.IRecipeMatcher;
import evilcraft.api.recipes.custom.IRecipeOutput;
import evilcraft.api.recipes.custom.IRecipeProperties;
import evilcraft.api.recipes.custom.IRecipeRegistry;
import evilcraft.api.recipes.custom.ISuperRecipeRegistry;

/**
 * Default implementation of {@link IRecipeRegistry}.
 * @author immortaleeb
 *
 * @param <M> The type of the machine.
 * @param <I> The type of the recipe input of all recipes associated with the machine.
 * @param <O> The type of the recipe output of all recipes associated with the machine.
 * @param <P> The type of the recipe properties of all recipes associated with the machine.
 */
public class RecipeRegistry<M extends IMachine<M, I, O, P>, I extends IRecipeInput, O extends IRecipeOutput, P extends IRecipeProperties>
	implements IRecipeRegistry<M, I, O, P> {
	
    private final M machine;

    /**
     * Make a new instance.
     * @param machine The machine this registry is for.
     */
    public RecipeRegistry(M machine) {
        this.machine = machine;
    }

    @Override
	public void registerRecipe(IRecipe<I, O, P> recipe) {
        RegistryManager.getRegistry(ISuperRecipeRegistry.class).getRecipes(machine).add(recipe);
    }

    @Override
    public void registerRecipe(String namedId, I input, O output, P properties) {
        registerRecipe(new Recipe<I, O, P>(namedId, input, output, properties));
    }

    @Override
    public void registerRecipe(I input, O output, P properties) {
        registerRecipe(new Recipe<I, O, P>(input, output, properties));
    }

    @Override
    public IRecipe<I, O, P> findRecipeByNamedId(String namedId) {
        return findRecipe(new RecipePropertyMatcher<M, IRecipe<I, O, P>, String>(namedId) {
            @Override
            public String getProperty(M machine, IRecipe<I, O, P> recipe) {
                return recipe.getNamedId();
            }
        });
    }

    @Override
    public IRecipe<I, O, P> findRecipeByInput(I input) {
        return findRecipe(new RecipePropertyMatcher<M, IRecipe<I, O, P>, I>(input) {

            @Override
            public I getProperty(M machine, IRecipe<I, O, P> recipe) {
                return recipe.getInput();
            }
        });
    }

    @Override
    public List<IRecipe<I, O, P>> findRecipesByInput(I input) {
        return findRecipes(new RecipePropertyMatcher<M, IRecipe<I, O, P>, I>(input) {

            @Override
            public I getProperty(M machine, IRecipe<I, O, P> recipe) {
                return recipe.getInput();
            }
        });
    }

    @Override
    public IRecipe<I, O, P> findRecipeByOutput(O output) {
        return findRecipe(new RecipePropertyMatcher<M, IRecipe<I, O, P>, O>(output) {

            @Override
            public O getProperty(M machine, IRecipe<I, O, P> recipe) {
                return recipe.getOutput();
            }
        });
    }

    @Override
    public List<IRecipe<I, O, P>> findRecipesByOutput(O output) {
        return findRecipes(new RecipePropertyMatcher<M, IRecipe<I, O, P>, O>(output) {

            @Override
            public O getProperty(M machine, IRecipe<I, O, P> recipe) {
                return recipe.getOutput();
            }
        });
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public IRecipe<I, O, P> findRecipe(IRecipeMatcher<M, IRecipe<I, O, P>> recipeMatcher) {
        for (IRecipe r : RegistryManager.getRegistry(ISuperRecipeRegistry.class).getRecipes(machine)) {
            IRecipe<I, O, P> recipe = (IRecipe<I, O, P>)r;
            if (recipeMatcher.matches(machine, recipe))
                return recipe;
        }

        return null;
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<IRecipe<I, O, P>> findRecipes(IRecipeMatcher<M, IRecipe<I, O, P>> recipeMatcher) {
        List<IRecipe<I, O, P>> results = new ArrayList<IRecipe<I, O, P>>();

        for (IRecipe r : RegistryManager.getRegistry(ISuperRecipeRegistry.class).getRecipes(machine)) {
            IRecipe<I, O, P> recipe = (IRecipe<I, O, P>)r;
            if (recipeMatcher.matches(machine, recipe))
                results.add(recipe);
        }

        return results;
    }

    @Override
    public List<IRecipe<I, O, P>> allRecipes() {
        return findRecipes(new RecipeMatcher<M, IRecipe<I,O,P>>() {
            @SuppressWarnings("rawtypes")
			@Override
            public boolean matches(IMachine machine, IRecipe recipeToMatch) {
                return true;
            }
        });
    }
}