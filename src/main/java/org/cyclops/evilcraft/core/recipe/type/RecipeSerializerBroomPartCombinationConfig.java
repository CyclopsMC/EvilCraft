package org.cyclops.evilcraft.core.recipe.type;

import net.minecraft.world.item.crafting.CustomRecipe;
import org.cyclops.cyclopscore.config.extendedconfig.RecipeConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link RecipeDeadBush}.
 * @author rubensworks
 */
public class RecipeSerializerBroomPartCombinationConfig extends RecipeConfigCommon<RecipeBroomPartCombination, IModBase> {

    public RecipeSerializerBroomPartCombinationConfig() {
        super(EvilCraft._instance,
                "crafting_special_broom_part_combination",
                eConfig -> new CustomRecipe.Serializer<>(RecipeBroomPartCombination::new));
    }

}
