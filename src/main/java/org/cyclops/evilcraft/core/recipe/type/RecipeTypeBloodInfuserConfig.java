package org.cyclops.evilcraft.core.recipe.type;

import org.cyclops.cyclopscore.config.extendedconfig.RecipeTypeConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

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
        RegistryEntries.RECIPETYPE_BLOOD_INFUSER = getInstance();
    }

}
