package evilcraft.core.recipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import evilcraft.api.recipes.custom.IMachine;
import evilcraft.api.recipes.custom.IRecipe;
import evilcraft.api.recipes.custom.IRecipeInput;
import evilcraft.api.recipes.custom.IRecipeMatch;
import evilcraft.api.recipes.custom.IRecipeMatcher;
import evilcraft.api.recipes.custom.IRecipeOutput;
import evilcraft.api.recipes.custom.IRecipeProperties;
import evilcraft.api.recipes.custom.ISuperRecipeRegistry;

/**
 * This class allows you to register new recipes for machines.
 * @see evilcraft.api.recipes.custom.IRecipe
 * @see evilcraft.api.recipes.custom.IRecipeInput
 * @see evilcraft.api.recipes.custom.IRecipeOutput
 * @see evilcraft.api.recipes.custom.IRecipeProperties
 * @see evilcraft.api.recipes.custom.IMachine
 * @author immortaleeb
 */
public class SuperRecipeRegistry implements ISuperRecipeRegistry {
    private static final Map<IMachine, List<IRecipe>> recipes = new HashMap<IMachine, List<IRecipe>>();
    private static final Map<IMachine, RecipeRegistry> registries = new HashMap<IMachine, RecipeRegistry>();

    @Override
    public <M extends IMachine<M, I, O, P>, I extends IRecipeInput, O extends IRecipeOutput, P extends IRecipeProperties>
        RecipeRegistry<M, I, O, P> getRecipeRegistry(M machine) {

        RecipeRegistry<M, I, O, P> registry = registries.get(machine);

        if (registry == null) {
            registry = new RecipeRegistry<M, I, O, P>(machine);
            registries.put(machine, registry);
        }

        return registry;
    }

    @Override
    public List<IRecipe> getRecipes(IMachine machine) {
        List<IRecipe> list = recipes.get(machine);
        if (list == null) {
            list = new ArrayList<IRecipe>();
            recipes.put(machine, list);
        }
        return list;
    }

    @Override
    public IRecipeMatch<IMachine, IRecipe> findRecipeByNamedId(String namedId) {
        return new RecipePropertyMatcher<IMachine, IRecipe, String>(namedId) {
            @Override
            public String getProperty(IMachine machine, IRecipe recipe) {
                return recipe.getNamedId();
            }
        }.findRecipe();
    }

    @Override
    public List<IRecipeMatch<IMachine, IRecipe>> findRecipesByInput(IRecipeInput input) {
        return new RecipePropertyMatcher<IMachine, IRecipe, IRecipeInput>(input) {
            @Override
            public IRecipeInput getProperty(IMachine machine, IRecipe recipe) {
                return recipe.getInput();
            }
        }.findRecipes();
    }

    @Override
    public List<IRecipeMatch<IMachine, IRecipe>> findRecipesByMachine(IMachine machine) {
        List<IRecipeMatch<IMachine, IRecipe>> list = new ArrayList<IRecipeMatch<IMachine, IRecipe>>();

        for (IRecipe recipe : getRecipes(machine)) {
            list.add(new RecipeMatch<IMachine, IRecipe>(machine, recipe));
        }

        return list;
    }

    @Override
    public <M extends IMachine, R extends IRecipe> RecipeMatch<M, R> findRecipe(IRecipeMatcher<M, R> recipeMatcher) {
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

    @Override
    public <M extends IMachine, R extends IRecipe> List<IRecipeMatch<M, R>> findRecipes(IRecipeMatcher<M, R> recipeMatcher) {
        List<IRecipeMatch<M, R>> results = new ArrayList<IRecipeMatch<M, R>>();

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
    
}
