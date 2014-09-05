package evilcraft.entities.tileentities.tickaction.bloodchest;

import net.minecraft.item.ItemStack;
import evilcraft.blocks.BloodChestConfig;
import evilcraft.core.entities.tileentitites.tickaction.ITickAction;
import evilcraft.entities.tileentities.TileBloodChest;

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
    
    private void drainTank(TileBloodChest tile) {
        tile.getTank().drain(BloodChestConfig.mBPerDamage, true);
    }

    @Override
    public void onTick(TileBloodChest tile, ItemStack itemStack, int slot, int tick) {
        if(tick >= getRequiredTicks(tile, slot)) {
            if(
                    !tile.getTank().isEmpty()
                    && tile.getTank().getFluidAmount() >= BloodChestConfig.mBPerDamage
                    && itemStack != null
                    ) {
                // Call handlers registered via API.
                int actionID = BloodChestRepairActionRegistry.canRepair(itemStack, tick);
                if(actionID > -1) {
                    drainTank(tile);
                    BloodChestRepairActionRegistry.repair(itemStack, tile.getWorldObj().rand, actionID);
                }
                
            }
        }
    }

    @Override
    public int getRequiredTicks(TileBloodChest tile, int slot) {
        return BloodChestConfig.ticksPerDamage;
    }
    
}
