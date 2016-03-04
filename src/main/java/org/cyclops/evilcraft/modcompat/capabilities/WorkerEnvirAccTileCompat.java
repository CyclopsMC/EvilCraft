package org.cyclops.evilcraft.modcompat.capabilities;

import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.cyclopscore.modcompat.ICapabilityCompat;
import org.cyclops.evilcraft.Capabilities;
import org.cyclops.evilcraft.tileentity.TileEnvironmentalAccumulator;

/**
 * Compatibility for envir acc worker capabilities.
 * @author rubensworks
 */
public class WorkerEnvirAccTileCompat implements ICapabilityCompat<TileEnvironmentalAccumulator> {

    @Override
    public void attach(final TileEnvironmentalAccumulator provider) {
        provider.addCapabilityInternal(Capabilities.WORKER, new Worker(provider));
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
