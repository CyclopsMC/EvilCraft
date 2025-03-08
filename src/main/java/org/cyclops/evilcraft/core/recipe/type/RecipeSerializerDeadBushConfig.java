package org.cyclops.evilcraft.core.recipe.type;

import net.minecraft.world.item.crafting.CustomRecipe;
import org.cyclops.cyclopscore.config.extendedconfig.RecipeConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link RecipeDeadBush}.
 * @author rubensworks
 */
public class RecipeSerializerDeadBushConfig extends RecipeConfigCommon<RecipeDeadBush, IModBase> {

    public RecipeSerializerDeadBushConfig() {
        super(EvilCraft._instance,
                "crafting_special_dead_bush",
                eConfig -> new CustomRecipe.Serializer<>(RecipeDeadBush::new));
    }

}
