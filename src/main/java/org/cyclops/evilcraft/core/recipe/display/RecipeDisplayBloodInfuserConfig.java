package org.cyclops.evilcraft.core.recipe.display;

import org.cyclops.cyclopscore.config.extendedconfig.RecipeDisplayConfigCommon;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the drying basin recipe display.
 * @author rubensworks
 *
 */
public class RecipeDisplayBloodInfuserConfig extends RecipeDisplayConfigCommon<RecipeDisplayBloodInfuser, EvilCraft> {

    public RecipeDisplayBloodInfuserConfig() {
        super(
                EvilCraft._instance,
                "blood_infuser",
                eConfig -> RecipeDisplayBloodInfuser.TYPE
        );
    }
}
