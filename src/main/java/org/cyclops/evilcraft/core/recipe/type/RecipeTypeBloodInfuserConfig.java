package org.cyclops.evilcraft.core.recipe.type;

import org.cyclops.cyclopscore.config.extendedconfig.RecipeTypeConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the blood infuser recipe type.
 * @author rubensworks
 *
 */
public class RecipeTypeBloodInfuserConfig extends RecipeTypeConfig<RecipeBloodInfuser> {
    public RecipeTypeBloodInfuserConfig() {
        super(
                EvilCraft._instance,
                "blood_infuser"
        );
    }
}
