package org.cyclops.evilcraft.core.recipe.category;

import net.minecraft.world.item.crafting.RecipeBookCategory;
import org.cyclops.cyclopscore.config.extendedconfig.RecipeBookCategoryConfigCommon;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the blood infuser recipe book category.
 * @author rubensworks
 *
 */
public class RecipeBookCategoryBloodInfuserConfig extends RecipeBookCategoryConfigCommon<RecipeBookCategory, EvilCraft> {

    public RecipeBookCategoryBloodInfuserConfig() {
        super(
                EvilCraft._instance,
                "blood_infuser",
                createDefault()
        );
    }
}
