package org.cyclops.evilcraft.core.fluid;

import org.cyclops.evilcraft.RegistryEntries;

/**
 * An implicit blood converter.
 * @author rubensworks
 *
 */
public class BloodFluidConverter extends ImplicitFluidConverter {

    private static BloodFluidConverter _instance = new BloodFluidConverter();

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BloodFluidConverter getInstance() {
        if(_instance == null) {
            _instance = new BloodFluidConverter();
        }
        if(_instance.getTarget() == null) {
            _instance.resetTarget();
        }
        return _instance;
    }

    private BloodFluidConverter() {
        super(null);
    }

    protected void resetTarget() {
        setTarget(RegistryEntries.FLUID_BLOOD);
    }

}
