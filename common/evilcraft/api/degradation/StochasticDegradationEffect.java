package evilcraft.api.degradation;

import java.util.Random;

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
    
    /**
     * Make a new instance.
     * @param chance The chance on occuring. A value between 0 and 1.
     */
    public StochasticDegradationEffect(double chance) {
        this.chance = chance;
    }
    
    /**
     * Make a new instance.
     */
    public StochasticDegradationEffect() {
        this(1.0D);
    }
    
    @Override
    public boolean canRun(IDegradable degradable) {
        Random random = degradable.getWorld().rand;
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
