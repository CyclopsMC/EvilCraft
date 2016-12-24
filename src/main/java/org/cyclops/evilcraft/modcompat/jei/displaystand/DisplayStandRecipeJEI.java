package org.cyclops.evilcraft.modcompat.jei.displaystand;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.plugins.vanilla.crafting.ShapedOreRecipeWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.evilcraft.block.DisplayStand;
import org.cyclops.evilcraft.core.recipe.DisplayStandRecipe;

import java.util.Arrays;
import java.util.List;

/**
 * Recipe wrapper for Blood Infuser recipes
 * @author rubensworks
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class DisplayStandRecipeJEI extends ShapedOreRecipeWrapper {

    private final List<ItemStack> outputs;
    private final IJeiHelpers jeiHelpers;
    private final ShapedOreRecipe recipe;

    public DisplayStandRecipeJEI(IJeiHelpers jeiHelpers, DisplayStandRecipe recipe) {
        super(jeiHelpers, recipe);
        this.jeiHelpers = jeiHelpers;
        this.recipe = recipe;
        this.outputs = Lists.newArrayList();
        for (ItemStack plankWoodStack : OreDictionary.getOres("plankWood")) {
            int plankWoodMeta = plankWoodStack.getItemDamage();
            if (plankWoodMeta == OreDictionary.WILDCARD_VALUE) {
                NonNullList<ItemStack> plankWoodSubItems = NonNullList.create();
                plankWoodStack.getItem().getSubItems(plankWoodStack.getItem(), null, plankWoodSubItems);
                for (ItemStack plankWoodSubItem : plankWoodSubItems) {
                    IBlockState plankWoodBlockState = BlockHelpers.getBlockStateFromItemStack(plankWoodSubItem);
                    this.outputs.add(DisplayStand.getInstance()
                            .getTypedDisplayStandItem(plankWoodBlockState));
                }
            } else {
                IBlockState plankWoodBlockState = BlockHelpers.getBlockStateFromItemStack(plankWoodStack);
                this.outputs.add(DisplayStand.getInstance().
                        getTypedDisplayStandItem(plankWoodBlockState));
            }
        }
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        IStackHelper stackHelper = jeiHelpers.getStackHelper();
        List<List<ItemStack>> inputs = stackHelper.expandRecipeItemStackInputs(Arrays.asList(recipe.getInput()));
        ingredients.setInputLists(ItemStack.class, inputs);
        ingredients.setOutputs(ItemStack.class, outputs);
    }
}
