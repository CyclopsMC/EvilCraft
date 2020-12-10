package org.cyclops.evilcraft.tileentity.tickaction.spiritreanimator;

import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.cyclops.cyclopscore.helper.InventoryHelpers;
import org.cyclops.evilcraft.block.BlockSpiritReanimatorConfig;
import org.cyclops.evilcraft.core.helper.MathHelpers;
import org.cyclops.evilcraft.core.tileentity.tickaction.ITickAction;
import org.cyclops.evilcraft.core.tileentity.upgrade.UpgradeSensitiveEvent;
import org.cyclops.evilcraft.core.tileentity.upgrade.Upgrades;
import org.cyclops.evilcraft.tileentity.TileSpiritReanimator;

/**
 * {@link ITickAction} that is able to reanimate boxes with spirits.
 * @author rubensworks
 *
 */
public class ReanimateTickAction implements ITickAction<TileSpiritReanimator> {
    
    @Override
    public boolean canTick(TileSpiritReanimator tile, ItemStack itemStack, int slot, int tick) {
        return tile.getTank().getFluidAmount() >= getRequiredMb(tile, tick) && tile.canWork();
    }

	protected ItemStack getSpawnEgg(EntityType<?> entityType) {
		return new ItemStack(SpawnEggItem.getEgg(entityType));
	}

	@Override
	public void onTick(TileSpiritReanimator tile, ItemStack itemStack, int slot, int tick) {
		// Drain the tank a bit.
		tile.getTank().drain(getRequiredMb(tile, tick), IFluidHandler.FluidAction.EXECUTE);
		if(tick >= getRequiredTicks(tile, slot, tick)) {
			ItemStack spawnEgg = ItemStack.EMPTY;
			EntityType<?> entityType = tile.getEntityType();
			if(entityType != null) {
				spawnEgg = getSpawnEgg(entityType);
			}
			if(!spawnEgg.isEmpty() && addToProduceSlot(tile, spawnEgg)) {
				tile.getInventory().decrStackSize(TileSpiritReanimator.SLOT_EGG, 1);
			}
			if(BlockSpiritReanimatorConfig.clearBoxContents) {
				itemStack.setTag(new CompoundNBT());
				tile.getInventory().setInventorySlotContents(TileSpiritReanimator.SLOT_BOX, itemStack);
			}
		}
	}

    protected int getRequiredMb(TileSpiritReanimator tile, int tick) {
        MutableDouble drain = new MutableDouble(BlockSpiritReanimatorConfig.mBPerTick);
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<MutableDouble>(drain, TileSpiritReanimator.UPGRADEEVENT_BLOODUSAGE));
        return MathHelpers.factorToBursts(drain.getValue(), tick);
    }

	@Override
	public float getRequiredTicks(TileSpiritReanimator tile, int slot, int tick) {
        MutableDouble drain = new MutableDouble(BlockSpiritReanimatorConfig.requiredTicks);
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<MutableDouble>(drain, TileSpiritReanimator.UPGRADEEVENT_SPEED));
        return (int) (double) drain.getValue();
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
