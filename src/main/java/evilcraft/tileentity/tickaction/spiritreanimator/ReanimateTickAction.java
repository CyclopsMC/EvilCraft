package evilcraft.tileentity.tickaction.spiritreanimator;

import evilcraft.block.SpiritReanimatorConfig;
import evilcraft.core.helper.InventoryHelpers;
import evilcraft.core.tileentity.tickaction.ITickAction;
import evilcraft.core.tileentity.upgrade.UpgradeSensitiveEvent;
import evilcraft.core.tileentity.upgrade.Upgrades;
import evilcraft.tileentity.TileSpiritFurnace;
import evilcraft.tileentity.TileSpiritReanimator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.mutable.MutableInt;

/**
 * {@link ITickAction} that is able to reanimate boxes with spirits.
 * @author rubensworks
 *
 */
public class ReanimateTickAction implements ITickAction<TileSpiritReanimator> {
    
    @Override
    public boolean canTick(TileSpiritReanimator tile, ItemStack itemStack, int slot, int tick) {
        return tile.getTank().getFluidAmount() >= getRequiredMb(tile) && tile.canWork();
    }
    
    protected ItemStack getCookStack(TileSpiritFurnace tile) {
        return tile.getInventory().getStackInSlot(tile.getConsumeSlot());
    }

	@Override
	public void onTick(TileSpiritReanimator tile, ItemStack itemStack, int slot,
			int tick) {
		// Drain the tank a bit.
		tile.getTank().drain(getRequiredMb(tile), true);
		if(tick >= getRequiredTicks(tile, slot)) {
			int entityID = tile.getEntityID();
			if(SpiritReanimatorConfig.clearBoxContents) {
				itemStack.setTagCompound(new NBTTagCompound());
			}
			if(entityID > -1) {
				ItemStack eggStack = new ItemStack((Item)Item.itemRegistry.getObject("spawn_egg"), 1, entityID);
				if(addToProduceSlot(tile, eggStack)) {
					tile.getInventory().decrStackSize(TileSpiritReanimator.SLOT_EGG, 1);
				}
			}
		}
	}

    protected int getRequiredMb(TileSpiritReanimator tile) {
        MutableInt drain = new MutableInt(SpiritReanimatorConfig.mBPerTick);
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<MutableInt>(drain, TileSpiritReanimator.UPGRADEEVENT_BLOODUSAGE));
        return Math.max(1, drain.getValue());
    }

	@Override
	public int getRequiredTicks(TileSpiritReanimator tile, int slot) {
        MutableInt drain = new MutableInt(SpiritReanimatorConfig.requiredTicks);
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<MutableInt>(drain, TileSpiritReanimator.UPGRADEEVENT_SPEED));
        return drain.getValue();
	}
	
	/**
     * Try to add the given item to the production slot.
     * @param tile The tile where reanimation happened.
     * @param itemStack The item to try to put in the output slot.
     * @return If the item could be added or joined in the output slot.
     */
    public boolean addToProduceSlot(TileSpiritReanimator tile, ItemStack itemStack) {
    	return InventoryHelpers.addToSlot(tile.getInventory(), TileSpiritReanimator.SLOTS_OUTPUT, itemStack);
    }
    
}
