package org.cyclops.evilcraft.tileentity.tickaction.spiritfurnace;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.block.SpiritFurnaceConfig;
import org.cyclops.evilcraft.core.helper.MathHelpers;
import org.cyclops.evilcraft.core.helper.obfuscation.ObfuscationHelpers;
import org.cyclops.evilcraft.core.tileentity.tickaction.ITickAction;
import org.cyclops.evilcraft.core.tileentity.upgrade.UpgradeSensitiveEvent;
import org.cyclops.evilcraft.core.tileentity.upgrade.Upgrades;
import org.cyclops.evilcraft.core.world.FakeWorldItemDelegator;
import org.cyclops.evilcraft.tileentity.TileSpiritFurnace;

/**
 * {@link ITickAction} that is able to cook boxes with spirits.
 * @author rubensworks
 *
 */
public class BoxCookTickAction implements ITickAction<TileSpiritFurnace> {
    
    @Override
    public boolean canTick(TileSpiritFurnace tile, ItemStack itemStack, int slot, int tick) {
        if(!tile.isForceHalt() && !tile.isCaughtError() && tile.canWork()
                && tile.getTank().getFluidAmount() >= getRequiredMb(tile, 0)
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
            BlockPos pos = tile.getPos();
			EvilCraft.proxy.sendSoundMinecraft(pos.getX() + 0.5D, pos.getY() + 0.5D,
					pos.getZ() + 0.5D, deathSound, 0.5F + world.rand.nextFloat() * 0.2F, 1.0F);
			
			try {
				// Kill the entity to get the drops
				entity.onDeath(DamageSource.generic);
			} catch (Exception e) { // Gotta catch 'em all
				tile.caughtError();
			}
		}
    }

    protected int getRequiredMb(TileSpiritFurnace tile, int tick) {
        MutableDouble drain = new MutableDouble(SpiritFurnaceConfig.mBPerTick);
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<MutableDouble>(drain, TileSpiritFurnace.UPGRADEEVENT_BLOODUSAGE));
        return MathHelpers.factorToBursts(drain.getValue(), tick);
    }

	@Override
	public void onTick(TileSpiritFurnace tile, ItemStack itemStack, int slot,
			int tick) {
		// Drain the tank a bit.
		tile.getTank().drain(getRequiredMb(tile, tick), true);
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
            requiredTicksBase = (int) ((entity.getHealth() + entity.getTotalArmorValue()) * SpiritFurnaceConfig.requiredTicksPerHp);
        }
        MutableDouble duration = new MutableDouble(requiredTicksBase);
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<MutableDouble>(duration, TileSpiritFurnace.UPGRADEEVENT_SPEED));
        return (int) (double) duration.getValue();
	}
    
}
