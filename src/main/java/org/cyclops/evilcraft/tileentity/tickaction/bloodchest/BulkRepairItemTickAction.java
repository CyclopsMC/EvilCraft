package org.cyclops.evilcraft.tileentity.tickaction.bloodchest;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.api.tileentity.bloodchest.IBloodChestRepairActionRegistry;
import org.cyclops.evilcraft.block.BlockColossalBloodChestConfig;
import org.cyclops.evilcraft.core.helper.MathHelpers;
import org.cyclops.evilcraft.core.tileentity.tickaction.ITickAction;
import org.cyclops.evilcraft.core.tileentity.upgrade.UpgradeSensitiveEvent;
import org.cyclops.evilcraft.core.tileentity.upgrade.Upgrades;
import org.cyclops.evilcraft.tileentity.TileColossalBloodChest;

/**
 * {@link ITickAction} that can repair items using blood.
 * @author rubensworks
 *
 */
public class BulkRepairItemTickAction implements ITickAction<TileColossalBloodChest> {
    
    @Override
    public boolean canTick(TileColossalBloodChest tile, ItemStack itemStack, int slot, int tick) {
        return tile.canWork() && !tile.getTank().isEmpty() && !itemStack.isEmpty();
    }
    
    private void drainTank(TileColossalBloodChest tile, float usageMultiplier, int tick) {
        tile.getTank().drain(getRequiredFluid(tile, usageMultiplier, tick), IFluidHandler.FluidAction.EXECUTE);
    }

    protected int getRequiredFluid(TileColossalBloodChest tile, float usageMultiplier, int tick) {
        MutableFloat drain = new MutableFloat(Math.max(
                0.05,
                ((float) BlockColossalBloodChestConfig.baseMBPerDamage * usageMultiplier)
                        * (1 - ((float) tile.getEfficiency() / (TileColossalBloodChest.MAX_EFFICIENCY + 10)))
        ));
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<>(drain, TileColossalBloodChest.UPGRADEEVENT_BLOODUSAGE));
        return MathHelpers.factorToBursts(drain.getValue(), (int) tile.getWorld().getGameTime() + tick % 100);
    }

    @Override
    public void onTick(TileColossalBloodChest tile, ItemStack itemStack, int slot, int tick) {
        if(tick >= getRequiredTicks(tile, slot, tick)) {
            if(!tile.getTank().isEmpty() && !itemStack.isEmpty()) {
                itemStack = itemStack.copy();
                // Call handlers registered via API.
                IBloodChestRepairActionRegistry actions = EvilCraft._instance.getRegistryManager().
                        getRegistry(IBloodChestRepairActionRegistry.class);
                int actionID = actions.canRepair(itemStack, tick);
                if(actionID > -1) {
                    float simulateMultiplier = actions.repair(itemStack, tile.getWorld().rand, actionID, false, true).getLeft();
                    if(tile.getTank().getFluidAmount() >= getRequiredFluid(tile, simulateMultiplier, tick) * simulateMultiplier) {
                        // Make sure that increasing speed by upgrades does not increase efficiency any faster.
                        Boolean slotHistory = tile.getSlotTickHistory().get(slot);
                        if((slotHistory == null || !slotHistory)) {
                            tile.setEfficiency(Math.min(tile.getEfficiency() + 1, TileColossalBloodChest.MAX_EFFICIENCY));
                            tile.getSlotTickHistory().put(slot, true);
                        }
                        Pair<Float, ItemStack> repairResult = actions.repair(itemStack, tile.getWorld().rand, actionID, true, true);
                        itemStack = repairResult.getRight();
                        drainTank(tile, repairResult.getLeft(), tick);
                    }
                }
                tile.getInventory().setInventorySlotContents(slot, itemStack);

            }
        }
    }

    @Override
    public float getRequiredTicks(TileColossalBloodChest tile, int slot, int tick) {
        MutableFloat duration = new MutableFloat(BlockColossalBloodChestConfig.ticksPerDamage);
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<MutableFloat>(duration, TileColossalBloodChest.UPGRADEEVENT_SPEED));
        return duration.getValue();
    }
    
}
