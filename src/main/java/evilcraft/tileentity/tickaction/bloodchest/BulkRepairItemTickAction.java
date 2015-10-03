package evilcraft.tileentity.tickaction.bloodchest;

import evilcraft.api.RegistryManager;
import evilcraft.api.tileentity.bloodchest.IBloodChestRepairActionRegistry;
import evilcraft.block.BloodChestConfig;
import evilcraft.core.tileentity.tickaction.ITickAction;
import evilcraft.core.tileentity.upgrade.UpgradeSensitiveEvent;
import evilcraft.core.tileentity.upgrade.Upgrades;
import evilcraft.tileentity.TileColossalBloodChest;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableFloat;

/**
 * {@link ITickAction} that can repair items using blood.
 * @author rubensworks
 *
 */
public class BulkRepairItemTickAction implements ITickAction<TileColossalBloodChest> {
    
    @Override
    public boolean canTick(TileColossalBloodChest tile, ItemStack itemStack, int slot, int tick) {
        return !tile.getTank().isEmpty() && itemStack != null;
    }
    
    private void drainTank(TileColossalBloodChest tile, float usageMultiplier) {
        tile.getTank().drain(getRequiredFluid(tile, usageMultiplier), true);
    }

    protected int getRequiredFluid(TileColossalBloodChest tile, float usageMultiplier) {
        MutableFloat duration = new MutableFloat((float) BloodChestConfig.mBPerDamage * usageMultiplier);
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<MutableFloat>(duration, TileColossalBloodChest.UPGRADEEVENT_BLOODUSAGE));
        return (int) Math.ceil(duration.getValue());
    }

    @Override
    public void onTick(TileColossalBloodChest tile, ItemStack itemStack, int slot, int tick) {
        // TODO: the more you process, the more efficient. Base usage must be higher though.
        if(tick >= getRequiredTicks(tile, slot)) {
            if(!tile.getTank().isEmpty() && itemStack != null) {
                // Call handlers registered via API.
                IBloodChestRepairActionRegistry actions = RegistryManager.
                        getRegistry(IBloodChestRepairActionRegistry.class);
                int actionID = actions.canRepair(itemStack, tick);
                if(actionID > -1) {
                    float simulateMultiplier = actions.repair(itemStack, tile.getWorldObj().rand, actionID, false);
                    if(tile.getTank().getFluidAmount() >= getRequiredFluid(tile, simulateMultiplier) * simulateMultiplier) {
                        float multiplier = actions.repair(itemStack, tile.getWorldObj().rand, actionID, true);
                        drainTank(tile, multiplier);
                    }
                }

            }
        }
    }

    @Override
    public int getRequiredTicks(TileColossalBloodChest tile, int slot) {
        MutableFloat duration = new MutableFloat(BloodChestConfig.ticksPerDamage);
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<MutableFloat>(duration, TileColossalBloodChest.UPGRADEEVENT_SPEED));
        return (int) Math.ceil(duration.getValue());
    }
    
}
