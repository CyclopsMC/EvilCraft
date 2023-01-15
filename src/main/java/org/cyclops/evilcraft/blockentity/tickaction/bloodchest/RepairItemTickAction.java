package org.cyclops.evilcraft.blockentity.tickaction.bloodchest;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.api.tileentity.bloodchest.IBloodChestRepairActionRegistry;
import org.cyclops.evilcraft.block.BlockBloodChestConfig;
import org.cyclops.evilcraft.blockentity.BlockEntityBloodChest;
import org.cyclops.evilcraft.core.blockentity.tickaction.ITickAction;

/**
 * {@link ITickAction} that can repair items using blood.
 * @author rubensworks
 *
 */
public class RepairItemTickAction implements ITickAction<BlockEntityBloodChest> {

    @Override
    public boolean canTick(BlockEntityBloodChest tile, ItemStack itemStack, int slot, int tick) {
        if(!tile.getTank().isEmpty() && !itemStack.isEmpty()) {
            // Call handlers registered via API.
            IBloodChestRepairActionRegistry actions = EvilCraft._instance.getRegistryManager().
                    getRegistry(IBloodChestRepairActionRegistry.class);
            int actionID = actions.canRepair(itemStack, tick);
            return actionID >= 0;
        }
        return false;
    }

    private void drainTank(BlockEntityBloodChest tile, float usageMultiplier) {
        tile.getTank().drain((int) Math.ceil((float) BlockBloodChestConfig.mBPerDamage * usageMultiplier), IFluidHandler.FluidAction.EXECUTE);
    }

    @Override
    public void onTick(BlockEntityBloodChest tile, ItemStack itemStack, int slot, int tick) {
        if(tick >= getRequiredTicks(tile, slot, tick)) {
            if(!tile.getTank().isEmpty() && !itemStack.isEmpty()) {
                itemStack = itemStack.copy();
                // Call handlers registered via API.
                IBloodChestRepairActionRegistry actions = EvilCraft._instance.getRegistryManager().
                        getRegistry(IBloodChestRepairActionRegistry.class);
                int actionID = actions.canRepair(itemStack, tick);
                if(actionID > -1) {
                    float simulateMultiplier = actions.repair(itemStack, tile.getLevel().random, actionID, false, false).getLeft();
                    if(tile.getTank().getFluidAmount() >= BlockBloodChestConfig.mBPerDamage * simulateMultiplier) {
                        Pair<Float, ItemStack> repairResult = actions.repair(itemStack, tile.getLevel().random, actionID, true, false);
                        itemStack = repairResult.getRight();
                        drainTank(tile, repairResult.getLeft());
                    }
                }
                tile.getInventory().setItem(slot, itemStack);
            }
        }
    }

    @Override
    public float getRequiredTicks(BlockEntityBloodChest tile, int slot, int tick) {
        return BlockBloodChestConfig.ticksPerDamage;
    }

}
