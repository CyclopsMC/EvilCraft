package evilcraft.entities.tileentities.tickaction.spiritfurnace;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import evilcraft.api.entities.tileentitites.tickaction.ITickAction;
import evilcraft.api.obfuscation.ObfuscationHelper;
import evilcraft.api.world.FakeWorld;
import evilcraft.api.world.FakeWorldItemDelegator;
import evilcraft.blocks.BoxOfEternalClosure;
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
    	// Only allow ticking if production slot is empty or if the producing item is the same and
        // there is at least one spot left in the stack.
        if(tile.canWork() && !tile.getTank().isEmpty() && getCookStack(tile) != null && tile.canConsume(getCookStack(tile))) {
            ItemStack production = tile.getInventory().getStackInSlot(tile.getProduceSlot());
            return production == null || production.stackSize < production.getMaxStackSize();
        }
        return false;
    }
    
    protected ItemStack getCookStack(TileSpiritFurnace tile) {
        return tile.getInventory().getStackInSlot(tile.getConsumeSlot());
    }
    
    protected EntityLiving getEntity(TileSpiritFurnace tile) {
    	ItemStack boxStack = getCookStack(tile);
    	if(boxStack.getItem() == tile.getAllowedCookItem()) {
    		String id = BoxOfEternalClosure.getInstance().getSpiritId(boxStack);
    		if(id != null) {
    			@SuppressWarnings("unchecked")
				Class<? extends EntityLivingBase> entityClass =
					(Class<? extends EntityLivingBase>) EntityList.stringToClassMapping.get(id);
    			if(entityClass != null) {
    				// TODO: profile this and maybe optimise
    				FakeWorld world = new FakeWorldItemDelegator(tile);
    				EntityLiving entity = (EntityLiving) EntityList.createEntityByName(id, world);
    				return entity;
    			}
    		}
    	}
    	return null;
    }
    
    protected void doNextDrop(TileSpiritFurnace tile) {
    	EntityLiving entity = getEntity(tile);
    	if(entity != null) {
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
		EntityLivingBase entity = getEntity(tile);
		if(entity == null) {
			return SpiritFurnaceConfig.requiredTicksPerHp;
		}
		return (int) (entity.getHealth() * SpiritFurnaceConfig.requiredTicksPerHp);
	}
    
}
