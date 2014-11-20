package evilcraft.tileentity.tickaction.spiritfurnace;

import evilcraft.EvilCraft;
import evilcraft.block.SpiritFurnaceConfig;
import evilcraft.core.helper.obfuscation.ObfuscationHelpers;
import evilcraft.core.tileentity.tickaction.ITickAction;
import evilcraft.core.tileentity.upgrade.UpgradeSensitiveEvent;
import evilcraft.core.tileentity.upgrade.Upgrades;
import evilcraft.core.world.FakeWorldItemDelegator;
import evilcraft.tileentity.TileSpiritFurnace;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import org.apache.commons.lang3.mutable.MutableInt;

/**
 * {@link ITickAction} that is able to cook boxes with spirits.
 * @author rubensworks
 *
 */
public class BoxCookTickAction implements ITickAction<TileSpiritFurnace> {
    
    @Override
    public boolean canTick(TileSpiritFurnace tile, ItemStack itemStack, int slot, int tick) {
        if(!tile.isForceHalt() && !tile.isCaughtError() && tile.canWork()
                && tile.getTank().getFluidAmount() >= getRequiredMb(tile)
        		&& getCookStack(tile) != null && tile.canConsume(getCookStack(tile))) {
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
			ObfuscationHelpers.setRecentlyHit(entity, 100);
			
			// Send sound to client
			String deathSound = ObfuscationHelpers.getDeathSound(entity);
			EvilCraft.proxy.sendSoundMinecraft(tile.xCoord + 0.5D, tile.yCoord + 0.5D,
					tile.zCoord + 0.5D, deathSound, 0.5F + world.rand.nextFloat() * 0.2F, 1.0F);
			
			try {
				// Kill the entity to get the drops
				entity.onDeath(DamageSource.generic);
			} catch (Exception e) { // Gotta catch 'em all
				tile.caughtError();
			}
		}
    }

    protected int getRequiredMb(TileSpiritFurnace tile) {
        MutableInt drain = new MutableInt(SpiritFurnaceConfig.mBPerTick);
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<MutableInt>(drain, TileSpiritFurnace.UPGRADEEVENT_BLOODUSAGE));
        return Math.max(1, drain.getValue());
    }

	@Override
	public void onTick(TileSpiritFurnace tile, ItemStack itemStack, int slot,
			int tick) {
		// Drain the tank a bit.
		tile.getTank().drain(getRequiredMb(tile), true);
		if(tick >= getRequiredTicks(tile, slot)) {
			doNextDrop(tile);
		}
	}

	@Override
	public int getRequiredTicks(TileSpiritFurnace tile, int slot) {
        int requiredTicksBase;
		EntityLivingBase entity = tile.getEntity();
		if(entity == null) {
            requiredTicksBase = SpiritFurnaceConfig.requiredTicksPerHp;
		} else {
            requiredTicksBase = (int) (entity.getHealth() * SpiritFurnaceConfig.requiredTicksPerHp);
        }
        MutableInt duration = new MutableInt(requiredTicksBase);
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<MutableInt>(duration, TileSpiritFurnace.UPGRADEEVENT_SPEED));
        return duration.getValue();
	}
    
}
