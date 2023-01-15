package org.cyclops.evilcraft.core.recipe.type;

import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
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
                eConfig -> new SimpleCraftingRecipeSerializer<>(RecipeDeadBush::new));
    }

}
