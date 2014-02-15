package evilcraft.api.config.configurable;

import evilcraft.api.config.ExtendedConfig;

/**
 * Interface for all elements that are configurable
 * @author rubensworks
 *
 */
public interface Configurable {
    
    /**
     * Set the config for this configurable.
     * @param eConfig
     */
    public void setConfig(@SuppressWarnings("rawtypes") ExtendedConfig eConfig);
    /**
     * Get the unique name.
     * @return The unique name.
     */
    public String getUniqueName();
    /**
     * Is this Configurable an entity?
     * @return If it is an entity.
     */
    public boolean isEntity();
    
}
