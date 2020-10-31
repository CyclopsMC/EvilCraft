package org.cyclops.evilcraft.core.recipe;

import net.minecraft.item.crafting.SpecialRecipeSerializer;
import org.cyclops.cyclopscore.config.extendedconfig.RecipeConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link RecipeDeadBush}.
 * @author rubensworks
 */
public class RecipeSerializerBroomPartCombinationConfig extends RecipeConfig<RecipeBroomPartCombination> {

    public RecipeSerializerBroomPartCombinationConfig() {
        super(EvilCraft._instance,
                "crafting_special_broom_part_combination",
                eConfig -> new SpecialRecipeSerializer<>(RecipeBroomPartCombination::new));
    }

}
