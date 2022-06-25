package org.cyclops.evilcraft.core.degradation;

import net.minecraft.util.RandomSource;
import org.cyclops.evilcraft.api.degradation.IDegradable;
import org.cyclops.evilcraft.api.degradation.IDegradationEffect;
import org.cyclops.evilcraft.core.config.extendedconfig.DegradationEffectConfig;


/**
 * A {@link IDegradationEffect} that can be executed with a certain chance.
 * It will take into account the current degradation factor and the higher this
 * is, the higher the chance of the execution of this effect.
 * An optional chance can also be given for a chance next to the degradation chance.
 * @author rubensworks
 *
 */
public abstract class StochasticDegradationEffect implements IDegradationEffect {

    private double chance;

    public StochasticDegradationEffect(DegradationEffectConfig eConfig, double chance) {
        this.chance = chance;
    }

    public StochasticDegradationEffect(DegradationEffectConfig eConfig) {
        this(eConfig, 1.0D);
    }

    @Override
    public boolean canRun(IDegradable degradable) {
        RandomSource random = degradable.getDegradationWorld().random;
        return degradable.getDegradation() * getChance() > random.nextDouble();
    }

    /**
     * Get the configured chance on occuring.
     * @return The chance.
     */
    public double getChance() {
        return this.chance;
    }

}
