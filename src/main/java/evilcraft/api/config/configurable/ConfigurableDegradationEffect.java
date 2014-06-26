package evilcraft.api.config.configurable;

import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.degradation.IDegradationEffect;

/**
 * Group interface of {@link Configurable} and {@link IDegradationEffect}.
 * @author rubensworks
 *
 */
public abstract class ConfigurableDegradationEffect implements Configurable, IDegradationEffect {

    @SuppressWarnings("rawtypes")
    protected ExtendedConfig eConfig = null;
    
    /**
     * The type of this {@link Configurable}.
     */
    public static ElementType TYPE = ElementType.DEGRADATIONEFFECT;

    /**
     * Make a new Degradation Effect instance
     * @param eConfig Config for this effect.
     */
    @SuppressWarnings("rawtypes")
    protected ConfigurableDegradationEffect(ExtendedConfig eConfig) {
        this.setConfig(eConfig);
        
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }
    
    @Override
    public String getUniqueName() {
        return "degradationeffects."+eConfig.NAMEDID;
    }

    @Override
    public boolean isEntity() {
        return false;
    }

}
