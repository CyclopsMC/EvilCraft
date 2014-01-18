package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ItemConfig;

public class DarkPowerGemConfig extends ItemConfig {
    
    public static DarkPowerGemConfig _instance;

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
