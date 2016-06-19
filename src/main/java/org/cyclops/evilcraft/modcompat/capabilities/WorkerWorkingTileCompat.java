package org.cyclops.evilcraft.modcompat.capabilities;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.cyclopscore.modcompat.capabilities.DefaultCapabilityProvider;
import org.cyclops.cyclopscore.modcompat.capabilities.SimpleCapabilityConstructor;
import org.cyclops.evilcraft.Capabilities;
import org.cyclops.evilcraft.core.tileentity.TickingTankInventoryTileEntity;
import org.cyclops.evilcraft.core.tileentity.tickaction.ITickAction;
import org.cyclops.evilcraft.core.tileentity.tickaction.TickComponent;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * Compatibility for worker capabilities.
 * @author rubensworks
 */
public class WorkerWorkingTileCompat extends SimpleCapabilityConstructor<IWorker, TickingTankInventoryTileEntity> {

    @Override
    public Capability<IWorker> getCapability() {
        return Capabilities.WORKER;
    }

    @Nullable
    @Override
    public ICapabilityProvider createProvider(TickingTankInventoryTileEntity host) {
        return new DefaultCapabilityProvider<IWorker>(Capabilities.WORKER, new Worker<TickingTankInventoryTileEntity>(host));
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
