package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ItemConfig;

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
            Reference.ITEM_DARKPOWERGEM,
            "Dark Power Gem",
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
