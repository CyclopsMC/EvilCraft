package org.cyclops.evilcraft.api.degradation;


/**
 * A set of actions that can apply to {@link IDegradable}.
 * @author rubensworks
 * @see IDegradationRegistry
 */
public interface IDegradationEffect {

    /**
     * If this effect can occur.
     * @param degradable The degradable instance where the effect should apply.
     * @return If it can occur.
     */
    public boolean canRun(IDegradable degradable);
    /**
     * Run the effect client side.
     * @param degradable The degradable instance where the effect should apply.
     */
    public void runClientSide(IDegradable degradable);
    /**
     * Run the effect server side.
     * @param degradable The degradable instance where the effect should apply.
     */
    public void runServerSide(IDegradable degradable);
    
}
