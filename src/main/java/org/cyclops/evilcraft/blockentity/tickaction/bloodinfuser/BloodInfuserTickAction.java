package org.cyclops.evilcraft.blockentity.tickaction.bloodinfuser;

import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableInt;
import org.cyclops.cyclopscore.helper.InventoryHelpers;
import org.cyclops.evilcraft.blockentity.BlockEntityBloodInfuser;
import org.cyclops.evilcraft.core.blockentity.tickaction.ITickAction;
import org.cyclops.evilcraft.core.blockentity.upgrade.UpgradeSensitiveEvent;
import org.cyclops.evilcraft.core.blockentity.upgrade.Upgrades;

/**
 * Abstract {@link ITickAction} that can infuse items with blood.
 * @author rubensworks
 *
 */
public abstract class BloodInfuserTickAction implements ITickAction<BlockEntityBloodInfuser> {

    protected final static int MB_PER_TICK = 100;

    @Override
    public boolean canTick(BlockEntityBloodInfuser tile, ItemStack itemStack, int slot, int tick) {
        // Only allow ticking if production slot is empty or if the producing item is the same and
        // there is at least one spot left in the stack.
        if(!tile.getTank().isEmpty() && getInfuseStack(tile) != null && tile.getTileWorkingMetadata().canConsume(getInfuseStack(tile), tile.getLevel())) {
            ItemStack production = tile.getInventory().getItem(tile.getTileWorkingMetadata().getProduceSlot());
            ItemStack willProduce = willProduceItem(tile);
            if(production.isEmpty()) {
                return true;
            } else if(!willProduce.isEmpty() && production.getItem() == willProduceItem(tile).getItem()) {
                if(production.getCount() + willProduce.getCount() <= production.getMaxStackSize())
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
    public ItemStack getInfuseStack(BlockEntityBloodInfuser tile) {
        return tile.getInventory().getItem(tile.getTileWorkingMetadata().getConsumeSlot());
    }

    /**
     * Get the item of the item that will be produced after infusion.
     * @param tile The tile that performs the infusion.
     * @return The item.
     */
    public abstract ItemStack willProduceItem(BlockEntityBloodInfuser tile);

    /**
     * Try to add the given item to the production slot.
     * @param tile The tile where infusion happened.
     * @param itemStack The item to try to put in the production slot.
     * @return If the item could be added or joined in the production slot.
     */
    public boolean addToProduceSlot(BlockEntityBloodInfuser tile, ItemStack itemStack) {
        return InventoryHelpers.addToSlot(tile.getInventory(), tile.getTileWorkingMetadata().getProduceSlot(), itemStack);
    }

    /**
     * Get the unmodified required conditions for the given conditions.
     * @param tile The tile entity that ticks.
     * @param slot The slot ID for the ticker.
     * @return Get the required ticks for the given slot for the given tile.
     */
    public abstract int getUnmodifiedRequiredTicks(BlockEntityBloodInfuser tile, int slot);

    @Override
    public final float getRequiredTicks(BlockEntityBloodInfuser tile, int slot, int tick) {
        MutableInt duration = new MutableInt(getUnmodifiedRequiredTicks(tile, slot));
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<MutableInt>(duration, BlockEntityBloodInfuser.UPGRADEEVENT_SPEED));
        return duration.getValue();
    }

}
