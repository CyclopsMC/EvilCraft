package org.cyclops.evilcraft.tileentity.tickaction.bloodinfuser;

import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableInt;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.cyclopscore.recipe.custom.component.ItemStackRecipeComponent;
import org.cyclops.evilcraft.core.recipe.custom.DurationXpRecipeProperties;
import org.cyclops.evilcraft.core.recipe.custom.ItemFluidStackAndTierRecipeComponent;
import org.cyclops.evilcraft.core.tileentity.tickaction.ITickAction;
import org.cyclops.evilcraft.core.tileentity.upgrade.UpgradeSensitiveEvent;
import org.cyclops.evilcraft.core.tileentity.upgrade.Upgrades;
import org.cyclops.evilcraft.tileentity.TileBloodInfuser;

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
        IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationXpRecipeProperties> recipe = getRecipe(tile);
        if(tick >= getRequiredTicks(tile, recipe)) {
            if(recipe != null) {
                if(addToProduceSlot(tile, recipe.getOutput().getItemStack().copy())) {
                    tile.getInventory().decrStackSize(tile.getConsumeSlot(), 1);
                    tile.getTank().drain(getRequiredFluidAmount(tile, recipe), true);
                }
            }
        }
    }

    protected int getRequiredFluidAmount(TileBloodInfuser tile, IRecipe<ItemFluidStackAndTierRecipeComponent,
            ItemStackRecipeComponent, DurationXpRecipeProperties> recipe) {
        MutableInt amount = new MutableInt(recipe.getInput().getFluidStack().amount);
        Upgrades.sendEvent(tile,
                new UpgradeSensitiveEvent<MutableInt>(amount, TileBloodInfuser.UPGRADEEVENT_BLOODUSAGE));
        return Math.max(1, amount.getValue());
    }
    
    private IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationXpRecipeProperties>
        getRecipe(TileBloodInfuser tile) {
        return tile.getRecipe(getInfuseStack(tile));
    }
    
    @Override
    public int getUnmodifiedRequiredTicks(TileBloodInfuser tile, int slot) {
        return getUnmodifiedRequiredTicks(tile, getRecipe(tile));
    }
    
    private int getUnmodifiedRequiredTicks(TileBloodInfuser tile,
                                 IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationXpRecipeProperties> recipe) {
        return recipe.getProperties().getDuration();
    }

    private int getRequiredTicks(TileBloodInfuser tile,
                                 IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationXpRecipeProperties> recipe) {
        MutableInt duration = new MutableInt(getUnmodifiedRequiredTicks(tile, recipe));
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<MutableInt>(duration, TileBloodInfuser.UPGRADEEVENT_SPEED));
        return duration.getValue();
    }
    
    @Override
    public ItemStack willProduceItem(TileBloodInfuser tile) {
        return getRecipe(tile).getOutput().getItemStack();
    }
    
}
