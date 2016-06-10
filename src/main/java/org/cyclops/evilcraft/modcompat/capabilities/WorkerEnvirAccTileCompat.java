package org.cyclops.evilcraft.modcompat.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.cyclopscore.modcompat.capabilities.DefaultCapabilityProvider;
import org.cyclops.cyclopscore.modcompat.capabilities.ICapabilityConstructor;
import org.cyclops.evilcraft.Capabilities;
import org.cyclops.evilcraft.tileentity.TileEnvironmentalAccumulator;

import javax.annotation.Nullable;

/**
 * Compatibility for envir acc worker capabilities.
 * @author rubensworks
 */
public class WorkerEnvirAccTileCompat implements ICapabilityConstructor<IWorker, TileEnvironmentalAccumulator> {

    @Override
    public Capability<IWorker> getCapability() {
        return Capabilities.WORKER;
    }

    @Nullable
    @Override
    public ICapabilityProvider createProvider(TileEnvironmentalAccumulator host) {
        return new DefaultCapabilityProvider<IWorker>(Capabilities.WORKER, new Worker(host));
    }

    public static class Worker implements IWorker {

        private final TileEnvironmentalAccumulator provider;

        public Worker(TileEnvironmentalAccumulator provider) {
            this.provider = provider;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean hasWork() {
            return provider.getRecipe() != null;
        }

        @Override
        public boolean canWork() {
            return true;
        }
    }
}
