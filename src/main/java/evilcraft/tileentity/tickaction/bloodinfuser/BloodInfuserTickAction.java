package evilcraft.tileentity.tickaction.bloodinfuser;

import evilcraft.core.helper.InventoryHelpers;
import evilcraft.core.tileentity.tickaction.ITickAction;
import evilcraft.core.tileentity.upgrade.UpgradeSensitiveEvent;
import evilcraft.core.tileentity.upgrade.Upgrades;
import evilcraft.tileentity.TileBloodInfuser;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableInt;

/**
 * Abstract {@link ITickAction} that can infuse items with blood.
 * @author rubensworks
 *
 */
public abstract class BloodInfuserTickAction implements ITickAction<TileBloodInfuser> {
    
    protected final static int MB_PER_TICK = 100;
    
    @Override
    public boolean canTick(TileBloodInfuser tile, ItemStack itemStack, int slot, int tick) {
        // Only allow ticking if production slot is empty or if the producing item is the same and
        // there is at least one spot left in the stack.
        if(!tile.getTank().isEmpty() && getInfuseStack(tile) != null && tile.canConsume(getInfuseStack(tile))) {
            ItemStack production = tile.getInventory().getStackInSlot(tile.getProduceSlot());
            ItemStack willProduce = willProduceItem(tile);
            if(production == null) {
                return true;
            } else if(willProduce != null && production.getItem() == willProduceItem(tile).getItem()) {
                if(production.stackSize + willProduce.stackSize <= production.getMaxStackSize())
                    return true;
            }                
        }
        return false;
    }
    
    /**
     * Get the stack in the infusion slot.
     * @param tile The tile to check the slot for.
     * @return The item in that slot.
     */
    public ItemStack getInfuseStack(TileBloodInfuser tile) {
        return tile.getInventory().getStackInSlot(tile.getConsumeSlot());
    }
    
    /**
     * Get the item of the item that will be produced after infusion.
     * @param tile The tile that performs the infusion.
     * @return The item.
     */
    public abstract ItemStack willProduceItem(TileBloodInfuser tile);
    
    /**
     * Try to add the given item to the production slot.
     * @param tile The tile where infusion happened.
     * @param itemStack The item to try to put in the production slot.
     * @return If the item could be added or joined in the production slot.
     */
    public boolean addToProduceSlot(TileBloodInfuser tile, ItemStack itemStack) {
    	return InventoryHelpers.addToSlot(tile.getInventory(), tile.getProduceSlot(), itemStack);
    }

    /**
     * Get the unmodified required conditions for the given conditions.
     * @param tile The tile entity that ticks.
     * @param slot The slot ID for the ticker.
     * @return Get the required ticks for the given slot for the given tile.
     */
    public abstract int getUnmodifiedRequiredTicks(TileBloodInfuser tile, int slot);

    @Override
    public final int getRequiredTicks(TileBloodInfuser tile, int slot) {
        MutableInt duration = new MutableInt(getUnmodifiedRequiredTicks(tile, slot));
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<MutableInt>(duration, TileBloodInfuser.UPGRADEEVENT_SPEED));
        return duration.getValue();
    }
    
}
