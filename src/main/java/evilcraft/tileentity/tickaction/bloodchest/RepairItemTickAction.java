package evilcraft.tileentity.tickaction.bloodchest;

import net.minecraft.item.ItemStack;
import evilcraft.api.RegistryManager;
import evilcraft.api.tileentity.bloodchest.IBloodChestRepairActionRegistry;
import evilcraft.block.BloodChestConfig;
import evilcraft.core.tileentity.tickaction.ITickAction;
import evilcraft.tileentity.TileBloodChest;

/**
 * {@link ITickAction} that can repair items using blood.
 * @author rubensworks
 *
 */
public class RepairItemTickAction implements ITickAction<TileBloodChest> {
    
    @Override
    public boolean canTick(TileBloodChest tile, ItemStack itemStack, int slot, int tick) {
        return !tile.getTank().isEmpty() && itemStack != null;
    }
    
    private void drainTank(TileBloodChest tile, float usageMultiplier) {
        tile.getTank().drain((int) Math.ceil((float) BloodChestConfig.mBPerDamage * usageMultiplier), true);
    }

    @Override
    public void onTick(TileBloodChest tile, ItemStack itemStack, int slot, int tick) {
        if(tick >= getRequiredTicks(tile, slot, tick)) {
            if(!tile.getTank().isEmpty() && itemStack != null) {
                // Call handlers registered via API.
            	IBloodChestRepairActionRegistry actions = RegistryManager.
            			getRegistry(IBloodChestRepairActionRegistry.class);
                int actionID = actions.canRepair(itemStack, tick);
                if(actionID > -1) {
                    float simulateMultiplier = actions.repair(itemStack, tile.getWorldObj().rand, actionID, false, false);
                    if(tile.getTank().getFluidAmount() >= BloodChestConfig.mBPerDamage * simulateMultiplier) {
                        float multiplier = actions.repair(itemStack, tile.getWorldObj().rand, actionID, true, false);
                        drainTank(tile, multiplier);
                    }
                }
                
            }
        }
    }

    @Override
    public float getRequiredTicks(TileBloodChest tile, int slot, int tick) {
        return BloodChestConfig.ticksPerDamage;
    }
    
}
