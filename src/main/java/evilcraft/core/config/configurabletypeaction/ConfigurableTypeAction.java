package evilcraft.core.config.configurabletypeaction;

import net.minecraftforge.common.config.Configuration;
import evilcraft.EvilCraft;
import evilcraft.core.config.ConfigHandler;
import evilcraft.core.config.UndisableableConfigException;
import evilcraft.core.config.extendedconfig.ExtendedConfig;

/**
 * An action that is used to register Configurables.
 * Used inside of {@link ConfigHandler}.
 * @author rubensworks
 * @param <C> The subclass of ExtendedConfig
 * @see ConfigHandler
 */
public abstract class ConfigurableTypeAction<C extends ExtendedConfig<C>> {
    
    /**
     * The common run method for all the subtypes of {@link ConfigurableTypeAction}.
     * @param eConfig The config to be registered.
     * @param config The config file reference.
     */
    public void commonRun(ExtendedConfig<C> eConfig, Configuration config) {
    	if(eConfig.isDisableable()) {
    		preRun(eConfig.downCast(), config, true);
    	}
        if(eConfig.isEnabled()) {
            postRun(eConfig.downCast(), config);
        } else if(!eConfig.isDisableable()) {
            throw new UndisableableConfigException(eConfig);
        } else {
            onSkipRegistration(eConfig);
        }
    }
    
    protected void onSkipRegistration(ExtendedConfig<C> eConfig) {
        EvilCraft.log("Skipped registering "+eConfig.getNamedId());
    }
    
    /**
     * Logic that constructs the eConfig from for example a config file.
     * @param eConfig configuration holder.
     * @param config configuration from the config file.
     * @param startup If this is currently being run at the mod startup.
     */
    public abstract void preRun(C eConfig, Configuration config, boolean startup);
    /**
     * Logic to register the eConfig target.
     * @param eConfig configuration holder.
     * @param config configuration from the config file.
     */
    public abstract void postRun(C eConfig, Configuration config);
}
