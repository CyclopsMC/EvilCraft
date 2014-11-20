package evilcraft.tileentity.tickaction.bloodinfuser;

import evilcraft.api.recipes.custom.IRecipe;
import evilcraft.block.BloodInfuser;
import evilcraft.core.recipe.custom.DurationRecipeProperties;
import evilcraft.core.recipe.custom.ItemAndFluidStackRecipeComponent;
import evilcraft.core.recipe.custom.ItemStackRecipeComponent;
import evilcraft.core.tileentity.tickaction.ITickAction;
import evilcraft.core.tileentity.upgrade.UpgradeSensitiveEvent;
import evilcraft.core.tileentity.upgrade.Upgrades;
import evilcraft.tileentity.TileBloodInfuser;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableInt;

/**
 * {@link ITickAction} that can infuse items with blood.
 * @author rubensworks
 *
 */
public class InfuseItemTickAction extends BloodInfuserTickAction{

    @Override
    public boolean canTick(TileBloodInfuser tile, ItemStack itemStack, int slot, int tick) {
        return super.canTick(tile, itemStack, slot, tick) &&
                tile.getTank().getFluidAmount() >= getRequiredFluidAmount(tile, getRecipe(tile));
    }

    @Override
    public void onTick(TileBloodInfuser tile, ItemStack itemStack, int slot, int tick) {
        IRecipe<ItemAndFluidStackRecipeComponent, ItemStackRecipeComponent, DurationRecipeProperties> recipe = getRecipe(tile);
        if(tick >= getRequiredTicks(tile, recipe)) {
            if(recipe != null) {
                if(addToProduceSlot(tile, recipe.getOutput().getItemStack().copy())) {
                    tile.getInventory().decrStackSize(tile.getConsumeSlot(), 1);
                    tile.getTank().drain(getRequiredFluidAmount(tile, recipe), true);
                }
            }
        }
    }

    protected int getRequiredFluidAmount(TileBloodInfuser tile, IRecipe<ItemAndFluidStackRecipeComponent,
            ItemStackRecipeComponent, DurationRecipeProperties> recipe) {
        MutableInt amount = new MutableInt(recipe.getInput().getFluidStack().amount);
        Upgrades.sendEvent(tile,
                new UpgradeSensitiveEvent<MutableInt>(amount, TileBloodInfuser.UPGRADEEVENT_BLOODUSAGE));
        return Math.max(1, amount.getValue());
    }
    
    private IRecipe<ItemAndFluidStackRecipeComponent, ItemStackRecipeComponent, DurationRecipeProperties>
        getRecipe(TileBloodInfuser tile) {

        return BloodInfuser.getInstance().getRecipeRegistry().findRecipeByInput(
                new ItemAndFluidStackRecipeComponent(getInfuseStack(tile), tile.getTank().getFluid()));
    }
    
    @Override
    public int getUnmodifiedRequiredTicks(TileBloodInfuser tile, int slot) {
        return getUnmodifiedRequiredTicks(tile, getRecipe(tile));
    }
    
    private int getUnmodifiedRequiredTicks(TileBloodInfuser tile,
                                 IRecipe<ItemAndFluidStackRecipeComponent, ItemStackRecipeComponent, DurationRecipeProperties> recipe) {
        return recipe.getProperties().getDuration();
    }

    private int getRequiredTicks(TileBloodInfuser tile,
                                 IRecipe<ItemAndFluidStackRecipeComponent, ItemStackRecipeComponent, DurationRecipeProperties> recipe) {
        MutableInt duration = new MutableInt(getUnmodifiedRequiredTicks(tile, recipe));
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<MutableInt>(duration, TileBloodInfuser.UPGRADEEVENT_SPEED));
        return duration.getValue();
    }
    
    @Override
    public Item willProduceItem(TileBloodInfuser tile) {
        return getRecipe(tile).getOutput().getItemStack().getItem();
    }
    
}
