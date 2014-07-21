package evilcraft.entities.tileentities.tickaction.spiritfurnace;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import evilcraft.api.entities.tileentitites.tickaction.ITickAction;
import evilcraft.api.obfuscation.ObfuscationHelper;
import evilcraft.api.world.FakeWorldItemDelegator;
import evilcraft.blocks.SpiritFurnaceConfig;
import evilcraft.entities.tileentities.TileSpiritFurnace;

/**
 * {@link ITickAction} that is able to cook boxes with spirits.
 * @author rubensworks
 *
 */
public class BoxCookTickAction implements ITickAction<TileSpiritFurnace> {
    
    @Override
    public boolean canTick(TileSpiritFurnace tile, ItemStack itemStack, int slot, int tick) {
        if(!tile.isForceHalt() && tile.canWork() && !tile.getTank().isEmpty()
        		&& getCookStack(tile) != null && tile.canConsume(getCookStack(tile))) {
        	EntityLiving entity = null;
        	for(int slotId : tile.getProduceSlots()) {
	        	ItemStack production = tile.getInventory().getStackInSlot(slotId);
	            if(production == null || production.stackSize < production.getMaxStackSize()) {
	            	return tile.isSizeValidForEntity();
	            }
            }
        }
        return false;
    }
    
    protected ItemStack getCookStack(TileSpiritFurnace tile) {
        return tile.getInventory().getStackInSlot(tile.getConsumeSlot());
    }
    
    protected void doNextDrop(TileSpiritFurnace tile) {
    	EntityLiving entity = tile.getEntity();
    	if(entity != null) {
    		FakeWorldItemDelegator world = FakeWorldItemDelegator.getInstance();
			world.setItemDropListener(tile);
			// To make sure the entity actually will drop something.
			ObfuscationHelper.setRecentlyHit(entity, 100);
			entity.onDeath(DamageSource.generic);
		}
    }

	@Override
	public void onTick(TileSpiritFurnace tile, ItemStack itemStack, int slot,
			int tick) {
		// Drain the tank a bit.
		tile.getTank().drain(SpiritFurnaceConfig.mBPerTick, true);
		if(tick >= getRequiredTicks(tile, slot)) {
			doNextDrop(tile);
		}
	}

	@Override
	public int getRequiredTicks(TileSpiritFurnace tile, int slot) {
		EntityLivingBase entity = tile.getEntity();
		if(entity == null) {
			return SpiritFurnaceConfig.requiredTicksPerHp;
		}
		return (int) (entity.getHealth() * SpiritFurnaceConfig.requiredTicksPerHp);
	}
    
}
