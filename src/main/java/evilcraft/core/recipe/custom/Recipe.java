package evilcraft.core.recipe.custom;

import evilcraft.api.recipes.custom.IRecipe;
import evilcraft.api.recipes.custom.IRecipeInput;
import evilcraft.api.recipes.custom.IRecipeOutput;
import evilcraft.api.recipes.custom.IRecipeProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Basic recipe that can be registered with {@link SuperRecipeRegistry}.
 *
 * @param <I> The type of the recipe input.
 * @param <O> The type of the recipe output.
 * @param <P> The type of the additional recipe properties.
 * @author immortaleeb
 */
@RequiredArgsConstructor
@EqualsAndHashCode
public class Recipe<I extends IRecipeInput, O extends IRecipeOutput, P extends IRecipeProperties> implements IRecipe<I, O, P> {
    @Getter
    private final String namedId;
    @Getter
    private final I input;
    @Getter
    private final O output;
    @Getter
    private final P properties;

    public Recipe(I input, O output) {
        this.input = input;
        this.output = output;
        this.properties = null;
        this.namedId = generateNamedId();
    }

    public Recipe(I input, O output, P properties) {
        this.input = input;
        this.output = output;
        this.properties = properties;
        this.namedId = generateNamedId();
    }

    /**
     * Generates a default named id.
     * @return A unique default named id.
     */
    private String generateNamedId() {
        String namedId = input.getClass().getName() + "_"  + output.getClass().getName() + "_";

        if (properties != null)
            namedId += properties.getClass().getName() + "_";

        namedId += Integer.toString(input.hashCode())
                +  Integer.toString(output.hashCode());

        if (properties != null)
            namedId += Integer.toString(properties.hashCode());

        return namedId;
    }
}
