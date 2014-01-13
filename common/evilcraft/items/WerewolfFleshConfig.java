package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ItemConfig;

public class WerewolfFleshConfig extends ItemConfig {
    
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
