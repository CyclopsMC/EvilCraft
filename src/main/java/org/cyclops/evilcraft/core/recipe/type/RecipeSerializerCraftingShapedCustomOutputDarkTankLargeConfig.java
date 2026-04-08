package org.cyclops.evilcraft.core.recipe.type;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.ItemStackTemplate;
import org.cyclops.cyclopscore.config.extendedconfig.RecipeConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.cyclopscore.recipe.type.RecipeCraftingShapedCustomOutput;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityDarkTank;

/**
 * Config for vengeance pickaxe recipes.
 * @author rubensworks
 */
public class RecipeSerializerCraftingShapedCustomOutputDarkTankLargeConfig extends RecipeConfigCommon<RecipeCraftingShapedCustomOutput, IModBase> {

    public RecipeSerializerCraftingShapedCustomOutputDarkTankLargeConfig() {
        super(EvilCraft._instance,
                "crafting_shaped_custom_output_dark_tank_large",
                eConfig -> new RecipeCraftingShapedCustomOutput.Serializer(() -> new ItemStackTemplate(RegistryEntries.ITEM_DARK_TANK, DataComponentPatch.builder()
                        .set(org.cyclops.cyclopscore.RegistryEntries.COMPONENT_CAPACITY.get(), BlockEntityDarkTank.BASE_CAPACITY * 9)
                        .build())).getRecipeSerializer());
    }

}
