package org.cyclops.evilcraft.core.recipe.type;

import org.cyclops.cyclopscore.config.extendedconfig.RecipeConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the blood infuser recipe serializer.
 * @author rubensworks
 *
 */
public class RecipeSerializerBloodInfuserConfig extends RecipeConfigCommon<RecipeBloodInfuser, IModBase> {

    public RecipeSerializerBloodInfuserConfig() {
        super(
                EvilCraft._instance,
                "blood_infuser",
                eConfig -> new RecipeSerializerBloodInfuser()
        );
    }

}
