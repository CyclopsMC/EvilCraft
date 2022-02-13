package org.cyclops.evilcraft.tileentity.tickaction.bloodinfuser;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.apache.commons.lang3.mutable.MutableInt;
import org.cyclops.evilcraft.core.recipe.type.RecipeBloodInfuser;
import org.cyclops.evilcraft.core.tileentity.tickaction.ITickAction;
import org.cyclops.evilcraft.core.tileentity.upgrade.UpgradeSensitiveEvent;
import org.cyclops.evilcraft.core.tileentity.upgrade.Upgrades;
import org.cyclops.evilcraft.tileentity.TileBloodInfuser;

import java.util.Optional;

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
        Optional<RecipeBloodInfuser> recipe = getRecipe(tile);
        if(tick >= getRequiredTicks(tile, recipe)) {
            if(recipe.isPresent()) {
                if(addToProduceSlot(tile, recipe.get().getOutputItem().copy())) {
                    tile.getInventory().removeItem(tile.getTileWorkingMetadata().getConsumeSlot(), 1);
                    tile.getTank().drain(getRequiredFluidAmount(tile, recipe), IFluidHandler.FluidAction.EXECUTE);
                    tile.addXp(recipe.get().getXp());
                }
            }
        }
    }

    protected int getRequiredFluidAmount(TileBloodInfuser tile, Optional<RecipeBloodInfuser> recipe) {
        if (!recipe.isPresent()) {
            return Integer.MAX_VALUE;
        }
        MutableInt amount = new MutableInt(recipe.get().getInputFluid().getAmount());
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<>(amount, TileBloodInfuser.UPGRADEEVENT_BLOODUSAGE));
        return Math.max(1, amount.getValue());
    }
    
    private Optional<RecipeBloodInfuser> getRecipe(TileBloodInfuser tile) {
        return tile.getRecipe(getInfuseStack(tile));
    }
    
    @Override
    public int getUnmodifiedRequiredTicks(TileBloodInfuser tile, int slot) {
        return getUnmodifiedRequiredTicks(tile, getRecipe(tile));
    }
    
    private int getUnmodifiedRequiredTicks(TileBloodInfuser tile, Optional<RecipeBloodInfuser> recipe) {
        return recipe.map(RecipeBloodInfuser::getDuration).orElse(0);
    }

    private int getRequiredTicks(TileBloodInfuser tile,
                                 Optional<RecipeBloodInfuser> recipe) {
        MutableInt duration = new MutableInt(getUnmodifiedRequiredTicks(tile, recipe));
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<MutableInt>(duration, TileBloodInfuser.UPGRADEEVENT_SPEED));
        return duration.getValue();
    }
    
    @Override
    public ItemStack willProduceItem(TileBloodInfuser tile) {
        return getRecipe(tile)
                .map(recipe -> recipe.getOutputItem().copy())
                .orElse(ItemStack.EMPTY);
    }
    
}
