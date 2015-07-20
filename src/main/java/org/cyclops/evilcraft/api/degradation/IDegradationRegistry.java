package org.cyclops.evilcraft.api.degradation;

import org.cyclops.cyclopscore.init.IRegistry;

import java.util.Set;

/**
 * Interface for the degradation registry.
 * @author rubensworks
 */
public interface IDegradationRegistry extends IRegistry {

	/**
     * Register a new degradation effect.
     * @param nameID The unique name of this effect.
     * @param degradationEffect The effect to register.
     * @param weight The weight that this effect can occur.
     */
    public void registerDegradationEffect(String nameID, IDegradationEffect degradationEffect, int weight);

    /**
     * Get all the registered degradation effects.
     * @return The registered effects.
     */
    public Set<IDegradationEffect> getDegradationEffects();
    
    /**
     * Get a degradation effect based on a weighted random choice.
     * @return A weighted random effect.
     */
    public IDegradationEffect getRandomDegradationEffect();
    
}
