package org.cyclops.evilcraft.core.recipe.category;

import net.minecraft.world.item.crafting.RecipeBookCategory;
import org.cyclops.cyclopscore.config.extendedconfig.RecipeBookCategoryConfigCommon;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the environmental accumulator recipe book category.
 * @author rubensworks
 *
 */
public class RecipeBookCategoryEnvironmentalAccumulatorConfig extends RecipeBookCategoryConfigCommon<RecipeBookCategory, EvilCraft> {

    public RecipeBookCategoryEnvironmentalAccumulatorConfig() {
        super(
                EvilCraft._instance,
                "environmental_accumulator",
                createDefault()
        );
    }
}
