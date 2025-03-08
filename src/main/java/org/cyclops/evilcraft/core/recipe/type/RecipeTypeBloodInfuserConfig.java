package org.cyclops.evilcraft.core.recipe.type;

import org.cyclops.cyclopscore.config.extendedconfig.RecipeTypeConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the blood infuser recipe type.
 * @author rubensworks
 *
 */
public class RecipeTypeBloodInfuserConfig extends RecipeTypeConfigCommon<RecipeBloodInfuser, IModBase> {
    public RecipeTypeBloodInfuserConfig() {
        super(
                EvilCraft._instance,
                "blood_infuser"
        );
    }
}
