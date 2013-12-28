package evilcraft.api.config.elementtypeaction;

import java.util.HashMap;
import java.util.Map;

import evilcraft.EvilCraft;
import evilcraft.api.config.ConfigHandler;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;

import net.minecraftforge.common.Configuration;

/**
 * An action that can be used to register Configurables
 * @author Ruben Taelman
 *
 */
public abstract class IElementTypeAction {
    
    public static Map<ElementType, String> CATEGORIES = new HashMap<ElementType, String>();
    static {
        CATEGORIES.put(ElementType.ITEM, ConfigHandler.CATEGORY_ITEM);
        CATEGORIES.put(ElementType.BLOCK, ConfigHandler.CATEGORY_BLOCK);
        CATEGORIES.put(ElementType.BLOCKCONTAINER, ConfigHandler.CATEGORY_BLOCK);
        CATEGORIES.put(ElementType.MOB, ConfigHandler.CATEGORY_ENTITY);
        CATEGORIES.put(ElementType.ENTITY, ConfigHandler.CATEGORY_ENTITY);
        CATEGORIES.put(ElementType.LIQUID, ConfigHandler.CATEGORY_LIQUID);
        CATEGORIES.put(ElementType.ENCHANTMENT, ConfigHandler.CATEGORY_ENCHANTMENT);
    }
    
    public void commonRun(ExtendedConfig eConfig, Configuration config) {
        preRun(eConfig, config);
        if(eConfig.isEnabled()) {
            postRun(eConfig, config);
        } else if(!eConfig.isDisableable()) {
            throw new UndisableableConfigException(eConfig);
        } else {
            onSkipRegistration(eConfig);
        }
    }
    
    protected void onSkipRegistration(ExtendedConfig eConfig) {
        EvilCraft.log("Skipped registering "+eConfig.NAME);
    }
    
    /**
     * Logic that constructs the eConfig from for example a config file.
     * @param eConfig configuration holder.
     * @param config configuration from the config file.
     */
    public abstract void preRun(ExtendedConfig eConfig, Configuration config);
    /**
     * Logic to register the eConfig target.
     * @param eConfig configuration holder.
     * @param config configuration from the config file.
     */
    public abstract void postRun(ExtendedConfig eConfig, Configuration config);
}
