package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ExtendedConfig;

public class WerewolfFleshConfig extends ExtendedConfig {
    
    public static WerewolfFleshConfig _instance;

    public WerewolfFleshConfig() {
        super(
            Reference.ITEM_WEREWOLFFLESH,
            "WerewolfFlesh",
            "werewolfFlesh",
            null,
            WerewolfFlesh.class
        );
    }
    
}
