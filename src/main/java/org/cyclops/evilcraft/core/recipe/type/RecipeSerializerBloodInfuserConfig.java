package org.cyclops.evilcraft.core.recipe.type;

import org.cyclops.cyclopscore.config.extendedconfig.RecipeConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the blood infuser recipe serializer.
 * @author rubensworks
 *
 */
public class RecipeSerializerBloodInfuserConfig extends RecipeConfig<RecipeBloodInfuser> {

    public RecipeSerializerBloodInfuserConfig() {
        super(
                EvilCraft._instance,
                "blood_infuser",
                eConfig -> new RecipeSerializerBloodInfuser()
        );
    }

}
