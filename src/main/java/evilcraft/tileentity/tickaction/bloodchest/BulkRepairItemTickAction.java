package evilcraft.tileentity.tickaction.bloodchest;

import evilcraft.api.RegistryManager;
import evilcraft.api.tileentity.bloodchest.IBloodChestRepairActionRegistry;
import evilcraft.block.ColossalBloodChestConfig;
import evilcraft.core.helper.MathHelpers;
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
        return tile.canWork() && !tile.getTank().isEmpty() && itemStack != null;
    }
    
    private void drainTank(TileColossalBloodChest tile, float usageMultiplier, int tick) {
        tile.getTank().drain(getRequiredFluid(tile, usageMultiplier, tick), true);
    }

    protected int getRequiredFluid(TileColossalBloodChest tile, float usageMultiplier, int tick) {
        MutableFloat drain = new MutableFloat(((float) ColossalBloodChestConfig.baseMBPerDamage * usageMultiplier) * (float) (TileColossalBloodChest.MAX_EFFICIENCY + 10 - tile.getEfficiency()) / TileColossalBloodChest.MAX_EFFICIENCY);
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<MutableFloat>(drain, TileColossalBloodChest.UPGRADEEVENT_BLOODUSAGE));
        return MathHelpers.factorToBursts(drain.getValue(), (int) tile.getWorldObj().getWorldTime() + tick % 100);
    }

    @Override
    public void onTick(TileColossalBloodChest tile, ItemStack itemStack, int slot, int tick) {
        if(tick >= getRequiredTicks(tile, slot, tick)) {
            if(!tile.getTank().isEmpty() && itemStack != null) {
                // Call handlers registered via API.
                IBloodChestRepairActionRegistry actions = RegistryManager.
                        getRegistry(IBloodChestRepairActionRegistry.class);
                int actionID = actions.canRepair(itemStack, tick);
                if(actionID > -1) {
                    float simulateMultiplier = actions.repair(itemStack, tile.getWorldObj().rand, actionID, false, true);
                    if(tile.getTank().getFluidAmount() >= getRequiredFluid(tile, simulateMultiplier, tick) * simulateMultiplier) {
                        // Make sure that increasing speed by upgrades does not increase efficiency any faster.
                        Boolean slotHistory = tile.getSlotTickHistory().get(slot);
                        if((slotHistory == null || !slotHistory)) {
                            tile.setEfficiency(Math.min(tile.getEfficiency() + 1, TileColossalBloodChest.MAX_EFFICIENCY));
                            tile.getSlotTickHistory().put(slot, true);
                        }
                        float multiplier = actions.repair(itemStack, tile.getWorldObj().rand, actionID, true, true);
                        drainTank(tile, multiplier, tick);
                    }
                }

            }
        }
    }

    @Override
    public float getRequiredTicks(TileColossalBloodChest tile, int slot, int tick) {
        MutableFloat duration = new MutableFloat(ColossalBloodChestConfig.ticksPerDamage);
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<MutableFloat>(duration, TileColossalBloodChest.UPGRADEEVENT_SPEED));
        return duration.getValue();
    }
    
}
