package org.cyclops.evilcraft.core.config.configurable;

import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.evilcraft.api.degradation.IDegradationEffect;

/**
 * Group interface of {@link IConfigurable} and {@link IDegradationEffect}.
 * @author rubensworks
 *
 */
public abstract class ConfigurableDegradationEffect implements IConfigurable, IDegradationEffect {

    @SuppressWarnings("rawtypes")
    protected ExtendedConfig eConfig = null;

    /**
     * Make a new Degradation Effect instance
     * @param eConfig Config for this effect.
     */
    @SuppressWarnings("rawtypes")
    protected ConfigurableDegradationEffect(ExtendedConfig eConfig) {
        this.setConfig(eConfig);
        
    }
    
    @SuppressWarnings("rawtypes")
    private void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public ExtendedConfig<?> getConfig() {
        return eConfig;
    }

}
