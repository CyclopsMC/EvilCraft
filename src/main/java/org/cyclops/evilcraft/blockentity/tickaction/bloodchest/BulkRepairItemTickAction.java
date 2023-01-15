package org.cyclops.evilcraft.blockentity.tickaction.bloodchest;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.api.tileentity.bloodchest.IBloodChestRepairActionRegistry;
import org.cyclops.evilcraft.block.BlockColossalBloodChestConfig;
import org.cyclops.evilcraft.blockentity.BlockEntityColossalBloodChest;
import org.cyclops.evilcraft.core.blockentity.tickaction.ITickAction;
import org.cyclops.evilcraft.core.blockentity.upgrade.UpgradeSensitiveEvent;
import org.cyclops.evilcraft.core.blockentity.upgrade.Upgrades;
import org.cyclops.evilcraft.core.helper.MathHelpers;

/**
 * {@link ITickAction} that can repair items using blood.
 * @author rubensworks
 *
 */
public class BulkRepairItemTickAction implements ITickAction<BlockEntityColossalBloodChest> {

    @Override
    public boolean canTick(BlockEntityColossalBloodChest tile, ItemStack itemStack, int slot, int tick) {
        return tile.canWork() && !tile.getTank().isEmpty() && !itemStack.isEmpty();
    }

    private void drainTank(BlockEntityColossalBloodChest tile, float usageMultiplier, int tick) {
        tile.getTank().drain(getRequiredFluid(tile, usageMultiplier, tick), IFluidHandler.FluidAction.EXECUTE);
    }

    protected int getRequiredFluid(BlockEntityColossalBloodChest tile, float usageMultiplier, int tick) {
        MutableFloat drain = new MutableFloat(Math.max(
                0.05,
                ((float) BlockColossalBloodChestConfig.baseMBPerDamage * usageMultiplier)
                        * (1 - ((float) tile.getEfficiency() / (BlockEntityColossalBloodChest.MAX_EFFICIENCY + 10)))
        ));
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<>(drain, BlockEntityColossalBloodChest.UPGRADEEVENT_BLOODUSAGE));
        return MathHelpers.factorToBursts(drain.getValue(), (int) tile.getLevel().getGameTime() + tick % 100);
    }

    @Override
    public void onTick(BlockEntityColossalBloodChest tile, ItemStack itemStack, int slot, int tick) {
        if(tick >= getRequiredTicks(tile, slot, tick)) {
            if(!tile.getTank().isEmpty() && !itemStack.isEmpty()) {
                itemStack = itemStack.copy();
                // Call handlers registered via API.
                IBloodChestRepairActionRegistry actions = EvilCraft._instance.getRegistryManager().
                        getRegistry(IBloodChestRepairActionRegistry.class);
                int actionID = actions.canRepair(itemStack, tick);
                if(actionID > -1) {
                    float simulateMultiplier = actions.repair(itemStack, tile.getLevel().random, actionID, false, true).getLeft();
                    if(tile.getTank().getFluidAmount() >= getRequiredFluid(tile, simulateMultiplier, tick) * simulateMultiplier) {
                        // Make sure that increasing speed by upgrades does not increase efficiency any faster.
                        Boolean slotHistory = tile.getSlotTickHistory().get(slot);
                        if((slotHistory == null || !slotHistory)) {
                            tile.setEfficiency(Math.min(tile.getEfficiency() + 1, BlockEntityColossalBloodChest.MAX_EFFICIENCY));
                            tile.getSlotTickHistory().put(slot, true);
                        }
                        Pair<Float, ItemStack> repairResult = actions.repair(itemStack, tile.getLevel().random, actionID, true, true);
                        itemStack = repairResult.getRight();
                        drainTank(tile, repairResult.getLeft(), tick);
                    }
                }
                tile.getInventory().setItem(slot, itemStack);

            }
        }
    }

    @Override
    public float getRequiredTicks(BlockEntityColossalBloodChest tile, int slot, int tick) {
        MutableFloat duration = new MutableFloat(BlockColossalBloodChestConfig.ticksPerDamage);
        Upgrades.sendEvent(tile, new UpgradeSensitiveEvent<MutableFloat>(duration, BlockEntityColossalBloodChest.UPGRADEEVENT_SPEED));
        return duration.getValue();
    }

}
