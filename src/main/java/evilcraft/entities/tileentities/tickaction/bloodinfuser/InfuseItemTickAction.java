package evilcraft.entities.tileentities.tickaction.bloodinfuser;

import evilcraft.api.recipes.DurationRecipeProperties;
import evilcraft.api.recipes.IRecipe;
import evilcraft.api.recipes.ItemAndFluidStackRecipeComponent;
import evilcraft.api.recipes.ItemStackRecipeComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import evilcraft.api.entities.tileentitites.tickaction.ITickAction;
import evilcraft.blocks.BloodInfuser;
import evilcraft.entities.tileentities.TileBloodInfuser;

/**
 * {@link ITickAction} that can infuse items with blood.
 * @author rubensworks
 *
 */
public class InfuseItemTickAction extends BloodInfuserTickAction{

    @Override
    public void onTick(TileBloodInfuser tile, ItemStack itemStack, int slot, int tick) {
        IRecipe<ItemAndFluidStackRecipeComponent, ItemStackRecipeComponent, DurationRecipeProperties> recipe = getRecipe(tile);
        if(tick >= getRequiredTicks(tile, recipe)) {
            if(recipe != null) {
                if(addToProduceSlot(tile, recipe.getOutput().getItemStack().copy())) {
                    tile.getInventory().decrStackSize(tile.getConsumeSlot(), 1);
                    tile.getTank().drain(recipe.getInput().getFluidStack().amount, true);
                }
            }
        }
    }
    
    private IRecipe<ItemAndFluidStackRecipeComponent, ItemStackRecipeComponent, DurationRecipeProperties>
        getRecipe(TileBloodInfuser tile) {

        return BloodInfuser.getInstance().getRecipeRegistry().findRecipeByInput(
                new ItemAndFluidStackRecipeComponent(getInfuseStack(tile), tile.getTank().getFluid()));
    }
    
    @Override
    public int getRequiredTicks(TileBloodInfuser tile, int slot) {
        return getRequiredTicks(tile, getRecipe(tile));
    }
    
    private int getRequiredTicks(TileBloodInfuser tile,
                                 IRecipe<ItemAndFluidStackRecipeComponent, ItemStackRecipeComponent, DurationRecipeProperties> recipe) {
        return recipe.getProperties().getDuration();
    }
    
    @Override
    public Item willProduceItem(TileBloodInfuser tile) {
        return getRecipe(tile).getOutput().getItemStack().getItem();
    }
    
}
