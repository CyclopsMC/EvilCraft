package evilcraft.api.config.elementtypeaction;

import net.minecraftforge.common.config.Configuration;
import evilcraft.EvilCraft;
import evilcraft.api.config.ConfigHandler;
import evilcraft.api.config.ExtendedConfig;

/**
 * An action that can be used to register Configurables.
 * Used inside of {@link ConfigHandler}.
 * @author rubensworks
 * @param <C> The subclass of ExtendedConfig
 * @see ConfigHandler
 */
public abstract class IElementTypeAction<C extends ExtendedConfig<C>> {
    
    /**
     * The common run method for all the subtypes of {@link IElementTypeAction}.
     * @param eConfig The config to be registered.
     * @param config The config file reference.
     */
    public void commonRun(ExtendedConfig<C> eConfig, Configuration config) {
        preRun(eConfig.downCast(), config);
        if(eConfig.isEnabled()) {
            postRun(eConfig.downCast(), config);
        } else if(!eConfig.isDisableable()) {
            throw new UndisableableConfigException(eConfig);
        } else {
            onSkipRegistration(eConfig);
        }
    }
    
    protected void onSkipRegistration(ExtendedConfig<C> eConfig) {
        EvilCraft.log("Skipped registering "+eConfig.NAMEDID);
    }
    
    /**
     * Logic that constructs the eConfig from for example a config file.
     * @param eConfig configuration holder.
     * @param config configuration from the config file.
     */
    public abstract void preRun(C eConfig, Configuration config);
    /**
     * Logic to register the eConfig target.
     * @param eConfig configuration holder.
     * @param config configuration from the config file.
     */
    public abstract void postRun(C eConfig, Configuration config);
}
