package org.cyclops.evilcraft.core.recipe;

import net.minecraft.item.crafting.SpecialRecipeSerializer;
import org.cyclops.cyclopscore.config.extendedconfig.RecipeConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link RecipeDeadBush}.
 * @author rubensworks
 */
public class RecipeSerializerDeadBushConfig extends RecipeConfig<RecipeDeadBush> {

    public RecipeSerializerDeadBushConfig() {
        super(EvilCraft._instance,
                "crafting_special_dead_bush",
                eConfig -> new SpecialRecipeSerializer<>(RecipeDeadBush::new));
    }

}
