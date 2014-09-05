package evilcraft.item;

import evilcraft.core.config.ItemConfig;

/**
 * Config for the {@link DarkPowerGem}.
 * @author rubensworks
 *
 */
public class DarkPowerGemConfig extends ItemConfig {
    
    /**
     * The unique instance.
     */
    public static DarkPowerGemConfig _instance;

    /**
     * Make a new instance.
     */
    public DarkPowerGemConfig() {
        super(
        	true,
            "darkPowerGem",
            null,
            DarkPowerGem.class
        );
    }
    
    @Override
    public boolean blendAlpha() {
        return true;
    }
    
}
