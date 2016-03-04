package org.cyclops.evilcraft.modcompat.capabilities;

import net.minecraft.item.ItemStack;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.cyclopscore.modcompat.ICapabilityCompat;
import org.cyclops.evilcraft.Capabilities;
import org.cyclops.evilcraft.core.tileentity.TickingTankInventoryTileEntity;
import org.cyclops.evilcraft.core.tileentity.tickaction.ITickAction;
import org.cyclops.evilcraft.core.tileentity.tickaction.TickComponent;

import java.util.Collection;

/**
 * Compatibility for worker capabilities.
 * @author rubensworks
 */
public class WorkerWorkingTileCompat implements ICapabilityCompat<TickingTankInventoryTileEntity> {

    @Override
    public void attach(final TickingTankInventoryTileEntity provider) {
        provider.addCapabilityInternal(Capabilities.WORKER, new Worker<TickingTankInventoryTileEntity>(provider));
    }

    public static class Worker<T extends TickingTankInventoryTileEntity> implements IWorker {

        private final T provider;

        public Worker(T provider) {
            this.provider = provider;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean hasWork() {
            for(TickComponent ticker : (Collection<TickComponent>) provider.getTickers()) {
                ItemStack itemStack = provider.getInventory().getStackInSlot(ticker.getSlot());
                if(itemStack != null) {
                    ITickAction tickAction = ticker.getTickAction(itemStack.getItem());
                    if(tickAction.canTick(provider, itemStack, ticker.getSlot(), ticker.getTick())) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public boolean canWork() {
            return !provider.getWorld().isBlockPowered(provider.getPos());
        }
    }
}
