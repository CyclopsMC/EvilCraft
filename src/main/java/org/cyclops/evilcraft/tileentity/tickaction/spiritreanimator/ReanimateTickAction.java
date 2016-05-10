package org.cyclops.evilcraft.tileentity.tickaction.spiritreanimator;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.cyclops.cyclopscore.helper.InventoryHelpers;
import org.cyclops.evilcraft.block.SpiritReanimatorConfig;
import org.cyclops.evilcraft.core.helper.MathHelpers;
import org.cyclops.evilcraft.core.tileentity.tickaction.ITickAction;
import org.cyclops.evilcraft.core.tileentity.upgrade.UpgradeSensitiveEvent;
import org.cyclops.evilcraft.core.tileentity.upgrade.Upgrades;
import org.cyclops.evilcraft.tileentity.TileSpiritFurnace;
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
    
    protected ItemStack getCookStack(TileSpiritFurnace tile) {
        return tile.getInventory().getStackInSlot(tile.getConsumeSlot());
    }

	protected ItemStack getSpawnEgg(int entityId) {
		return new ItemStack(Item.itemRegistry.getObject(new ResourceLocation("spawn_egg")), 1, entityId);
	}

	protected ItemStack getSpawnEgg(String entityName) {
		ItemStack itemStack = new ItemStack((Item)Item.itemRegistry.getObject(new ResourceLocation("spawn_egg")));
		net.minecraft.nbt.NBTTagCompound nbt = new net.minecraft.nbt.NBTTagCompound();
		nbt.setString("entity_name", entityName);
		itemStack.setTagCompound(nbt);
		return itemStack;

	}

	@Override
	public void onTick(TileSpiritReanimator tile, ItemStack itemStack, int slot,
			int tick) {
		// Drain the tank a bit.
		tile.getTank().drain(getRequiredMb(tile, tick), true);
		if(tick >= getRequiredTicks(tile, slot, tick)) {
			ItemStack spawnEgg = null;
			String entityName = tile.getEntityName();
			if(entityName != null) {
				spawnEgg = getSpawnEgg(entityName);
			}
			if(spawnEgg != null && addToProduceSlot(tile, spawnEgg)) {
				tile.getInventory().decrStackSize(TileSpiritReanimator.SLOT_EGG, 1);
			}
			if(SpiritReanimatorConfig.clearBoxContents) {
				itemStack.setTagCompound(new NBTTagCompound());
			}
		}
	}

    protected int getRequiredMb(TileSpiritReanimator tile, int tick) {
        MutableDouble drain = new MutableDouble(SpiritReanimatorConfig.mBPerTick);
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<MutableDouble>(drain, TileSpiritReanimator.UPGRADEEVENT_BLOODUSAGE));
        return MathHelpers.factorToBursts(drain.getValue(), tick);
    }

	@Override
	public float getRequiredTicks(TileSpiritReanimator tile, int slot, int tick) {
        MutableDouble drain = new MutableDouble(SpiritReanimatorConfig.requiredTicks);
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
