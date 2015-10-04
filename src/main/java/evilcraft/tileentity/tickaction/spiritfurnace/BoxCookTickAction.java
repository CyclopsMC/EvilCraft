package evilcraft.tileentity.tickaction.spiritfurnace;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import evilcraft.EvilCraft;
import evilcraft.block.SpiritFurnaceConfig;
import evilcraft.core.helper.MathHelpers;
import evilcraft.core.helper.WeightedItemStack;
import evilcraft.core.helper.obfuscation.ObfuscationHelpers;
import evilcraft.core.tileentity.tickaction.ITickAction;
import evilcraft.core.tileentity.upgrade.UpgradeSensitiveEvent;
import evilcraft.core.tileentity.upgrade.Upgrades;
import evilcraft.core.world.FakeWorldItemDelegator;
import evilcraft.tileentity.TileSpiritFurnace;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import org.apache.commons.lang3.mutable.MutableDouble;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * {@link ITickAction} that is able to cook boxes with spirits.
 * @author rubensworks
 *
 */
public class BoxCookTickAction implements ITickAction<TileSpiritFurnace> {

    public static Map<Class<? extends EntityLivingBase>, List<WeightedItemStack>> MOBDROP_OVERRIDES = Maps.newHashMap();

    static {
        if(SpiritFurnaceConfig.villagerDropEmeraldChance > 0) {
            overrideMobDrop(EntityVillager.class, Sets.newHashSet(
                    new WeightedItemStack(new ItemStack(Items.emerald), 1),
                    new WeightedItemStack(null, SpiritFurnaceConfig.villagerDropEmeraldChance - 1)
            ));
        }
    }

    /**
     * Override an entity's drops inside the spirit furnace.
     * @param entity The entity class.
     * @param drops A map of drops to relative frequency, with the second pair of the map key representing the min-max
     *              amount of drops (both inclusive)
     */
    public static void overrideMobDrop(Class<? extends EntityLivingBase> entity, Set<WeightedItemStack> drops) {
        MOBDROP_OVERRIDES.put(entity, WeightedItemStack.createWeightedList(drops));
    }
    
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
			
			// Send sound to client
			String deathSound = ObfuscationHelpers.getDeathSound(entity);
            if(SpiritFurnaceConfig.mobDeathSounds) {
                EvilCraft.proxy.sendSoundMinecraft(tile.xCoord + 0.5D, tile.yCoord + 0.5D,
                        tile.zCoord + 0.5D, deathSound, 0.5F + world.rand.nextFloat() * 0.2F, 1.0F);
            }

            if(MOBDROP_OVERRIDES.containsKey(entity.getClass())) {
                List<WeightedItemStack> possibleDrops = MOBDROP_OVERRIDES.get(entity.getClass());
                WeightedItemStack weightedItemStack = WeightedItemStack.getRandomWeightedItemStack(possibleDrops, world.rand);
                ItemStack drop = weightedItemStack.getItemStackWithRandomizedSize(world.rand);
                if(drop != null) {
                    tile.onItemDrop(drop);
                }
            } else {
                // To make sure the entity actually will drop something.
                ObfuscationHelpers.setRecentlyHit(entity, 100);

                try {
                    // Kill the entity to get the drops
                    entity.onDeath(DamageSource.generic);
                } catch (Exception e) { // Gotta catch 'em all
                    tile.caughtError();
                }
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
		if(tick >= getRequiredTicks(tile, slot, tick)) {
			doNextDrop(tile);
		}
	}

	@Override
	public float getRequiredTicks(TileSpiritFurnace tile, int slot, int tick) {
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
