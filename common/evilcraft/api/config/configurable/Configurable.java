package evilcraft.api.config.configurable;

import evilcraft.api.config.ExtendedConfig;

/**
 * Interface for all elements that are configurable
 * @author Ruben Taelman
 *
 */
public interface Configurable {
    public void setConfig(ExtendedConfig eConfig);
    public String getUniqueName();
    public boolean isEntity();
}
