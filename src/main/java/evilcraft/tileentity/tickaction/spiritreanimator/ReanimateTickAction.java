package evilcraft.tileentity.tickaction.spiritreanimator;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import evilcraft.block.SpiritReanimatorConfig;
import evilcraft.core.helper.InventoryHelpers;
import evilcraft.core.tileentity.tickaction.ITickAction;
import evilcraft.tileentity.TileSpiritFurnace;
import evilcraft.tileentity.TileSpiritReanimator;

/**
 * {@link ITickAction} that is able to reanimate boxes with spirits.
 * @author rubensworks
 *
 */
public class ReanimateTickAction implements ITickAction<TileSpiritReanimator> {
    
    @Override
    public boolean canTick(TileSpiritReanimator tile, ItemStack itemStack, int slot, int tick) {
        return !tile.getTank().isEmpty() && tile.canWork();
    }
    
    protected ItemStack getCookStack(TileSpiritFurnace tile) {
        return tile.getInventory().getStackInSlot(tile.getConsumeSlot());
    }

	@Override
	public void onTick(TileSpiritReanimator tile, ItemStack itemStack, int slot,
			int tick) {
		// Drain the tank a bit.
		tile.getTank().drain(SpiritReanimatorConfig.mBPerTick, true);
		if(tick >= getRequiredTicks(tile, slot)) {
			if(SpiritReanimatorConfig.clearBoxContents) {
				itemStack.setTagCompound(new NBTTagCompound());
			}
			int entityID = tile.getEntityID();
			if(entityID > -1) {
				ItemStack eggStack = new ItemStack((Item)Item.itemRegistry.getObject("spawn_egg"), 1, entityID);
				if(addToProduceSlot(tile, eggStack)) {
					tile.getInventory().decrStackSize(TileSpiritReanimator.SLOT_EGG, 1);
				}
			}
		}
	}

	@Override
	public int getRequiredTicks(TileSpiritReanimator tile, int slot) {
		return SpiritReanimatorConfig.requiredTicks;
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
