package org.cyclops.evilcraft.core.recipe;

import org.cyclops.cyclopscore.config.extendedconfig.RecipeConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link RecipeDisplayStand}.
 * @author rubensworks
 */
public class RecipeSerializerDisplayStandConfig extends RecipeConfig<RecipeDisplayStand> {

    public RecipeSerializerDisplayStandConfig() {
        super(EvilCraft._instance,
                "crafting_special_display_stand",
                eConfig -> new RecipeSerializerDisplayStand());
    }

}
